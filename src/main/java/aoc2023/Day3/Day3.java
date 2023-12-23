package aoc2023.Day3;

import utils.FileUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day3 {
    FileUtils fileUtils = new FileUtils();
    String fileLines;
    ArrayList<char[]> map = new ArrayList<>();
    ArrayList<EnginePartSequence> enginePartSequences = new ArrayList<>();
    ArrayList<Gear> gearsList = new ArrayList<>();


    public Day3() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day3/input.txt", "\n");

        fillMap(fileLines);
    }

    private void fillMap(String fileLines) {
        Pattern pattern = Pattern.compile("[0-9]+|\\*");
        AtomicInteger counter = new AtomicInteger(0);
        Arrays.stream(fileLines.split("\n")).forEach(line -> {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                if (matcher.group().equals("*")) {
                    gearsList.add(new Gear(matcher.start(), counter.get()));
                } else {
                    enginePartSequences.add(new EnginePartSequence(matcher.start(), matcher.end() - 1, Integer.parseInt(matcher.group()), counter.get()));
                }
            }
            map.add(line.toCharArray());
            counter.set(counter.get() + 1);
        });
    }

    boolean checkIfNeighbourIsSpecialCharacter(char charToCheck) {
        return charToCheck != '.' && !Character.isDigit(charToCheck);
    }

    boolean isSpecialCharacterNeighbourTop(int start, int end, int row) {
        if (row - 1 < 0) return false;
        int tmpStartIndex = start - 1 >= 0 ? start - 1 : start;
        int tmpEndIndex = end + 1 < map.get(row).length ? end + 1 : end;
        for (int i = tmpStartIndex; i <= tmpEndIndex; i++) {
            if (checkIfNeighbourIsSpecialCharacter(map.get(row - 1)[i])) {
                return true;
            }
        }
        return false;
    }

    boolean isSpecialCharacterNeighbourDown(int start, int end, int row) {
        if (row + 1 >= map.size()) return false;
        int tmpStartIndex = start - 1 >= 0 ? start - 1 : start;
        int tmpEndIndex = end + 1 < map.get(row).length ? end + 1 : end;
        for (int i = tmpStartIndex; i <= tmpEndIndex; i++) {
            if (checkIfNeighbourIsSpecialCharacter(map.get(row + 1)[i])) {
                return true;
            }
        }
        return false;
    }

    boolean isSpacialCharacterNeighbourLeft(int start, int row) {
        if (start > 0) return checkIfNeighbourIsSpecialCharacter(map.get(row)[start - 1]);
        return false;
    }

    boolean isSpecialCharacterNeighbourRight(int end, int row) {
        if ((end + 1) < map.get(row).length) return checkIfNeighbourIsSpecialCharacter(map.get(row)[end + 1]);
        return false;
    }

    public int resolvePart1() {
        int sum;
        sum = enginePartSequences.stream()
                .filter(part -> isSpecialCharacterNeighbourTop(part.start, part.end, part.row) || isSpecialCharacterNeighbourDown(part.start, part.end, part.row)
                        || isSpacialCharacterNeighbourLeft(part.start, part.row) || isSpecialCharacterNeighbourRight(part.end, part.row))
                .map(part -> part.value).reduce(0, Integer::sum);

        return sum;
    }

    List<EnginePartSequence> findPartNeighbours(Gear gear) {
        int tmpStartX = gear.x - 1 < 0 ? gear.x : gear.x - 1;
        int tmpEndX = gear.x + 1 >= map.get(gear.y).length ? gear.x : gear.x + 1;
        int tmpStartY = gear.y - 1 < 0 ? gear.y : gear.y - 1;
        int tmpEndY = gear.y + 1 >= map.size() ? gear.y : gear.y + 1;

        return enginePartSequences.stream()
                .filter(part -> part.interceptArea(tmpStartX, tmpEndX, tmpStartY, tmpEndY))
                .collect(Collectors.toList());
    }

    public int resolvePart2() {
        gearsList.forEach(gear -> {
            gear.partNeighbours = findPartNeighbours(gear);
        });
        return gearsList.stream()
                .filter(gear -> gear.partNeighbours.size() == 2)
                .map(gear -> gear.partNeighbours)
                .reduce(0, (partialResult, part) -> partialResult + part.stream()
                        .reduce(1, (a, b) -> a * b.value, Integer::sum), Integer::sum);
    }
}

class EnginePartSequence {
    int start, end, value, row;

    public EnginePartSequence(int start, int end, int value, int row) {
        this.start = start;
        this.end = end;
        this.value = value;
        this.row = row;
    }

    public boolean interceptArea(int startX, int endX, int startY, int endY) {
        if (this.row < startY || this.row > endY) return false;
        return (this.start >= startX && this.start <= endX) || (this.end >= startX && this.end <= endX);
    }
}

class Gear {
    int x, y;
    List<EnginePartSequence> partNeighbours = new ArrayList<>();

    public Gear(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
