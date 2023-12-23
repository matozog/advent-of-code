package aoc2023.Day9;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;
    List<int[]> histories = new ArrayList<>();


    public Day9() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day9/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        Arrays.stream(fLines.split("\n")).forEach(line -> {
            String[] historiesInString = line.split(" ");
            histories.add(Arrays.stream(historiesInString).mapToInt(Integer::parseInt).toArray());
        });
    }

    int[] getDifferencesArray(int[] values) {
        int[] differences = new int[values.length - 1];
        for (int i = 0; i < values.length - 1; i++) {
            differences[i] = values[i + 1] - values[i];
        }

        return differences;
    }

    int predictNextValue(int[] values) {
        if (Arrays.stream(values).allMatch(value -> value == 0)) return 0;

        return values[values.length - 1] + predictNextValue(getDifferencesArray(values));
    }

    int predictPreviousValue(int[] values) {
        if (Arrays.stream(values).allMatch(value -> value == 0)) return 0;

        return values[0] - predictPreviousValue(getDifferencesArray(values));
    }

    public int resolvePart1() {
        return histories.stream().map(this::predictNextValue).mapToInt(Integer::intValue).sum();
    }

    public int resolvePart2() {
        return histories.stream().map(this::predictPreviousValue).mapToInt(Integer::intValue).sum();
    }
}
