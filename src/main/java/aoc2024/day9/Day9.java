package aoc2024.day9;

import commons.FileUtils;
import commons.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Day9 implements Task<Long> {
    String fileLine;
    int[] disk;
    List<String> fileSystem = new ArrayList<>();
    List<Block> blocksList = new ArrayList<>();

    public Day9() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day9/input.txt", "\n");
        String[] blocks = fileLine.split("");
        disk = Arrays.stream(blocks).mapToInt(Integer::parseInt).toArray();
        int id = 0;
        int blockId = 0;
        for (int i = 0; i < disk.length; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < disk[i]; j++) {
                    fileSystem.add(String.valueOf(id));
                }
                blocksList.add(new Block(id, blockId, blockId + disk[i] - 1, i));
                id++;
            } else {
                for (int j = 0; j < disk[i]; j++) {
                    fileSystem.add(".");
                }
                blocksList.add(new Block(-1, blockId, blockId + disk[i] - 1, i));
            }
            blockId += disk[i];
        }
    }

    List<String> createCompressedFiles() {
        List<String> compressed = new ArrayList<>(fileSystem);
        int firstIndexFreeSpace = compressed.indexOf(".");
        for (int i = compressed.size() - 1; i >= 0; i--) {
            if (firstIndexFreeSpace >= i) break;
            String element = compressed.get(i);
            if (!element.equals(".")) {
                Collections.swap(compressed, firstIndexFreeSpace, i);
                firstIndexFreeSpace = compressed.indexOf(".");
            }
        }
        return compressed;
    }

    @Override
    public Long resolvePart1() {
        int[] compressedFiles = createCompressedFiles().stream()
                .filter(value -> !value.equals("."))
                .mapToInt(Integer::parseInt)
                .toArray();

        return IntStream.range(0, compressedFiles.length)
                .map(index -> compressedFiles[index] * index)
                .mapToLong(Long::valueOf)
                .reduce(0L, Long::sum);
    }

    Block findFreeSpaceBlockForMemory(Block memoryBlock) {
        int memoryBlockLength = memoryBlock.stopIndex - memoryBlock.startIndex + 1;
        for (Block freeSpace : blocksList.stream()
                .filter(block -> block.id == -1 && block.stopIndex < memoryBlock.startIndex)
                .toList()) {
            int freeSpaceLength = freeSpace.stopIndex - freeSpace.startIndex + 1;
            if (memoryBlockLength <= freeSpaceLength) return freeSpace;
        }

        return null;
    }

    void compressBlocks() {
        List<Block> memory = blocksList.stream().filter(block -> block.id != -1).toList();
        for (int i = memory.size() - 1; i >= 0; i--) {
            Block memoryBlock = memory.get(i);
            Block freeSpaceBlock = findFreeSpaceBlockForMemory(memoryBlock);
            if (freeSpaceBlock != null) {
                int tmpFreeSpaceBlockEnd = freeSpaceBlock.stopIndex;
                freeSpaceBlock.setStopIndex(freeSpaceBlock.startIndex + memoryBlock.blockLength - 1);

                if (freeSpaceBlock.stopIndex < tmpFreeSpaceBlockEnd) {
                    int freeSpaceIndex = blocksList.indexOf(freeSpaceBlock);
                    blocksList.add(freeSpaceIndex + 1, new Block(-1, freeSpaceBlock.stopIndex + 1, tmpFreeSpaceBlockEnd, freeSpaceIndex + 1));
                }
                freeSpaceBlock.id = memoryBlock.id;
                memoryBlock.id = -1;
            }
        }
    }

    @Override
    public Long resolvePart2() {
        compressBlocks();

        return blocksList.stream()
                .filter(block -> block.id != -1)
                .map(Block::getSum)
                .reduce(0L, Long::sum);
    }
}

class Block {
    int id, startIndex, stopIndex, blockLength, index;

    public Block(int id, int startIndex, int stopIndex, int index) {
        blockLength = stopIndex - startIndex + 1;
        this.id = id;
        this.stopIndex = stopIndex;
        this.startIndex = startIndex;
        this.index = index;
    }

    public void setStopIndex(int stopIndex) {
        this.stopIndex = stopIndex;
        this.blockLength = stopIndex - startIndex + 1;
    }

    public long getSum() {
        long sum = 0L;
        for (int i = startIndex; i <= stopIndex; i++) {
            sum += (long) i * id;
        }
        return sum;
    }
}
