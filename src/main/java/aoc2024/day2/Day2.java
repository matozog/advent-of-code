package aoc2024.day2;

import commons.FileUtils;
import commons.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day2 implements Task<Integer> {
    FileUtils fileUtils = new FileUtils();
    String fileLines;

    public Day2() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day2/input.txt", "\n");
    }

    boolean isElementsIncreasingOrDecreasing(int el1, int el2, boolean increasing) {
        return increasing ? el1 < el2 : el1 > el2;
    }

    boolean isElementsDifferentAtMostThree(int el1, int el2) {
        return Math.abs(el1 - el2) <= 3;
    }

    boolean isLevelSafe(List<Integer> list) {
        if (Objects.equals(list.get(0), list.get(1))) return false;
        boolean increasing = list.get(0) < list.get(1);

        return IntStream.range(0, list.size() - 1)
                .allMatch((index) -> isElementsIncreasingOrDecreasing(list.get(index), list.get(index + 1), increasing)
                        && isElementsDifferentAtMostThree(list.get(index), list.get(index + 1)));
    }

    boolean calculateLevelSafety(String level, boolean error) {
        List<Integer> list = Arrays.stream(level.split(" "))
                .map(Integer::parseInt)
                .toList();

        boolean safeLevel = isLevelSafe(list);

        if (error && !safeLevel) {
            return IntStream.range(0, list.size())
                    .mapToObj(i -> IntStream.range(0, list.size())
                            .filter(index -> index != i)
                            .mapToObj(list::get)
                            .collect(Collectors.toList()))
                    .anyMatch(this::isLevelSafe);
        }

        return safeLevel;
    }

    @Override
    public Integer resolvePart1() {
        return Arrays.stream(fileLines.split("\n")).filter(line -> calculateLevelSafety(line, false)).toList().size();
    }

    @Override
    public Integer resolvePart2() {
        return Arrays.stream(fileLines.split("\n")).filter(line -> calculateLevelSafety(line, true)).toList().size();
    }
}
