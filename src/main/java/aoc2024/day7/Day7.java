package aoc2024.day7;

import commons.FileUtils;
import commons.Task;
import commons.Utils;

import java.util.*;

public class Day7 implements Task<Long> {
    FileUtils fileUtils = new FileUtils();
    String fileLine;
    Map<String, List<Long>> equations = new HashMap<>();

    public Day7() {
        fileLine = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day7/input.txt", "\n");

        Arrays.stream(fileLine.split("\n")).forEach(line -> {
            String[] resultAndValues = line.split(":");
            equations.put(resultAndValues[0], Arrays.stream(resultAndValues[1].trim().split(" "))
                    .map(Long::parseLong).toList());
        });
    }

    long generateResultFromOperators(char[] operators, List<Long> equation) {
        long result = equation.get(0);
        for (int i = 0; i < operators.length; i++) {
            if (operators[i] == '+') {
                result += equation.get(i + 1);
            } else if (operators[i] == '*') {
                result *= equation.get(i + 1);
            } else if (operators[i] == '|') {
                result *= (long) Math.pow(10L, String.valueOf(equation.get(i + 1)).length());
                result += equation.get(i + 1);
            }
        }

        return result;
    }

    boolean isPossibleToCalibrate(long equationResult, List<Long> equation, char[] possibleChars) {
        List<char[]> operators = new ArrayList<>();
        Utils.generateCharactersPermutationArray(operators, possibleChars, "", equation.size() - 1);

        return operators.stream()
                .map((permutation -> generateResultFromOperators(permutation, equation)))
                .anyMatch(result -> result == equationResult);
    }

    @Override
    public Long resolvePart1() {

        return equations.keySet()
                .stream()
                .filter(key -> isPossibleToCalibrate(Long.parseLong(key), equations.get(key), new char[]{'*', '+'}))
                .mapToLong(Long::parseLong)
                .reduce(0L, Long::sum);
    }

    @Override
    public Long resolvePart2() {
        return equations.keySet()
                .stream()
                .filter(key -> isPossibleToCalibrate(Long.parseLong(key), equations.get(key), new char[]{'*', '+', '|'}))
                .mapToLong(Long::parseLong)
                .reduce(0L, Long::sum);
    }
}
