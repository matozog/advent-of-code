package aoc2023.Day13;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day13 {
    FileUtils fileUtils = new FileUtils();
    String fileLines;
    List<Pattern> patternList = new ArrayList<>();

    public Day13() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day13/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        Arrays.stream(fLines.split("\n\n")).forEach(patternLine -> {
            Pattern pattern = new Pattern(patternLine.split("\n").length);
            AtomicInteger index = new AtomicInteger();
            Arrays.stream(patternLine.split("\n")).forEach(line -> {
                pattern.map[index.get()] = line.split("");
                index.set(index.get() + 1);
            });
            patternList.add(pattern);
        });
    }

    public int resolvePart1() {
        int verticalSum = 0, horizontalSum = 0;
        for (Pattern pattern : patternList) {
            pattern.calculateLineOfReflection('N', -1);
            if (pattern.lineOfReflectionType == 'V') {
                verticalSum += pattern.lineOfReflection;
            } else if (pattern.lineOfReflectionType == 'H') {
                horizontalSum += pattern.lineOfReflection;
            }
        }
        return verticalSum + 100 * horizontalSum;
    }

    void findNewLineOfReflectionForPattern(Pattern pattern) {
        pattern.calculateLineOfReflection('N', -1);
        char tmpLineOfReflectionType = pattern.lineOfReflectionType;
        int tmpLineOfReflection = pattern.lineOfReflection;
        pattern.lineOfReflectionType = 'N';
        for (int i = 0; i < pattern.map.length; i++) {
            for (int j = 0; j < pattern.map[0].length; j++) {
                String tmpPatternType = pattern.map[i][j];
                pattern.map[i][j] = tmpPatternType.equals("#") ? "." : "#";
                pattern.calculateLineOfReflection(tmpLineOfReflectionType, tmpLineOfReflection);
                if (pattern.lineOfReflectionType == 'V') {
                    return;
                } else if (pattern.lineOfReflectionType == 'H') {
                    return;
                } else {
                    pattern.map[i][j] = tmpPatternType;
                }
            }
        }
    }

    public int resolvePart2() {
        int verticalSum = 0, horizontalSum = 0;

        for (Pattern pattern : patternList) {
            findNewLineOfReflectionForPattern(pattern);
            if (pattern.lineOfReflectionType == 'V') {
                verticalSum += pattern.lineOfReflection;
            } else if (pattern.lineOfReflectionType == 'H') {
                horizontalSum += pattern.lineOfReflection;
            }
        }
        return verticalSum + 100 * horizontalSum;
    }
}

class Pattern {
    String[][] map;
    int lineOfReflection;
    char lineOfReflectionType;

    public Pattern(int mapLength) {
        map = new String[mapLength][];
    }

    boolean verticalLinesAreSame(int line1, int line2) {
        for (String[] strings : map) {
            if (!strings[line1].equals(strings[line2])) {
                return false;
            }
        }
        return true;
    }

    int findVerticalReflectionLine(int reflectionLineToSkip) {
        for (int i = 1; i < map[0].length; i++) {
            if (i == reflectionLineToSkip) continue;
            int j = 0;
            boolean isReflection = false;
            while (j < i && (j + i < map[0].length)) {
                if (verticalLinesAreSame(i - j - 1, i + j)) {
                    isReflection = true;
                } else {
                    isReflection = false;
                    break;
                }
                j++;
            }
            if (isReflection) {
                return i;
            }
        }
        return -1;
    }

    int findHorizontalReflectionLine(int reflectionLineToSkip) {
        for (int i = 1; i < map.length; i++) {
            if (i == reflectionLineToSkip) continue;
            int j = 0;
            boolean isReflection = false;
            while (j < i && (j + i < map.length)) {
                if (String.join("", map[i - j - 1]).equals(String.join("", map[i + j]))) {
                    isReflection = true;
                } else {
                    isReflection = false;
                    break;
                }
                j++;
            }
            if (isReflection) {
                return i;
            }
        }
        return -1;
    }

    public void calculateLineOfReflection(char reflectionTypeToSkip, int reflectionLineToSkip) {
        int verticalReflectionLine = reflectionTypeToSkip == 'V' ?
                findVerticalReflectionLine(reflectionLineToSkip)
                : findVerticalReflectionLine(-1);
        int horizontalReflectionLine = reflectionTypeToSkip == 'H' ?
                findHorizontalReflectionLine(reflectionLineToSkip)
                : findHorizontalReflectionLine(-1);

        if (verticalReflectionLine != -1) {
            lineOfReflectionType = 'V';
            lineOfReflection = verticalReflectionLine;
        } else if (horizontalReflectionLine != -1) {
            lineOfReflection = horizontalReflectionLine;
            lineOfReflectionType = 'H';
        }
    }
}
