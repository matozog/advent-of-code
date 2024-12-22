package aoc2024.day22;

import commons.FileUtils;
import commons.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day22 implements Task<Long> {
    String fileLine;
    int maxSteps = 2000;
    List<Long> secretNumbers = new ArrayList<>();

    public Day22() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day22/input.txt", "\n");
        Arrays.stream(fileLine.split("\n")).forEach(line -> {
            secretNumbers.add(Long.parseLong(line));
        });
    }

    long mixNumber(long number, long secretNumber) {
        return number ^ secretNumber;
    }

    long pruneNumber(long secretNumber) {
        return secretNumber % 16777216L;
    }

    long calculateNewSecretNumber(long currentSecretNumber, int steps) {
        if (steps == maxSteps) return currentSecretNumber;

        long newSecret = currentSecretNumber;

        newSecret = pruneNumber(mixNumber(newSecret * 64, newSecret));

        newSecret = pruneNumber(mixNumber(Math.round(newSecret / 32), newSecret));

        newSecret = pruneNumber(mixNumber(newSecret * 2048, newSecret));

        return calculateNewSecretNumber(newSecret, steps + 1);
    }

    @Override
    public Long resolvePart1() {
        return secretNumbers.stream().map(number -> calculateNewSecretNumber(number, 0)).reduce(0L, Long::sum);
    }

    @Override
    public Long resolvePart2() {
        return 0L;
    }
}
