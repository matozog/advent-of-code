package aoc2024.day25;

import commons.FileUtils;
import commons.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 implements Task<Integer> {
    String fileLine;
    List<List<Integer>> locks = new ArrayList<>();
    List<List<Integer>> keys = new ArrayList<>();

    public Day25() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day25/input.txt", "\n");
        String[] locksAndKeys = fileLine.split("\n\n");
        for (String lockOrKey : locksAndKeys) {
            if (lockOrKey.startsWith("#")) {
                locks.add(createKeyOrLock(lockOrKey));
            } else {
                keys.add(createKeyOrLock(lockOrKey));
            }
        }
    }

    List<Integer> createKeyOrLock(String key) {
        String[] keyLines = key.split("\n");
        int[] newKey = new int[keyLines[0].length()];
        Arrays.fill(newKey, -1);
        for (String keyLine : keyLines) {
            char[] lineValues = keyLine.toCharArray();
            for (int j = 0; j < keyLine.length(); j++) {
                if (lineValues[j] == '#') {
                    newKey[j] = newKey[j] + 1;
                }
            }
        }

        return Arrays.stream(newKey).boxed().collect(Collectors.toList());
    }

    boolean doesKeyFitToLock(List<Integer> key, List<Integer> lock) {
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) + lock.get(i) > key.size()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer resolvePart1() {
        return keys.stream()
                .map(key -> locks.stream().filter(lock -> doesKeyFitToLock(key, lock)).toList().size())
                .reduce(0, Integer::sum);
    }

    @Override
    public Integer resolvePart2() {
        return 0;
    }
}


