package aoc2024.day11;

import commons.FileUtils;
import commons.Task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day11 implements Task<Long> {
    String fileLine;
    String[] stones;
    int maxSteps = 75;
    Map<Integer, Map<String, Long>> cache = new HashMap<>();

    public Day11() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day11/input.txt", "\n");
        stones = fileLine.split(" ");
    }

    Long calculateNumberOfStones(String stone, int steps, int maxSteps) {
        if (steps == maxSteps) return 1L;
        long result;

        cache.computeIfAbsent(steps, k -> new HashMap<>());

        Long cachedValue = cache.get(steps).get(stone);
        if (cachedValue != null) {
            return cachedValue;
        }

        if (stone.equals("0")) {
            result = calculateNumberOfStones("1", steps + 1, maxSteps);
        } else if (stone.length() % 2 == 0) {
            int halfIndex = stone.length() / 2;
            Long firstStone = calculateNumberOfStones(stone.substring(0, halfIndex), steps + 1, maxSteps);
            Long secondStone = calculateNumberOfStones(String.valueOf(Long.parseLong(stone.substring(halfIndex))), steps + 1, maxSteps);
            result = firstStone + secondStone;
        } else {
            result = calculateNumberOfStones(String.valueOf(Long.parseLong(stone) * 2024L), steps + 1, maxSteps);
        }

        cache.get(steps).putIfAbsent(stone, result);

        return result;
    }

    @Override
    public Long resolvePart1() {
        return Arrays.stream(stones)
                .map(stone -> calculateNumberOfStones(stone, 0, 25))
                .reduce(0L, Long::sum);
    }

    @Override
    public Long resolvePart2() {
        return Arrays.stream(stones)
                .map(stone -> calculateNumberOfStones(stone, 0, 75))
                .reduce(0L, Long::sum);
    }
}
