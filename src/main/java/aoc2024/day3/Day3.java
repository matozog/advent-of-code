package aoc2024.day3;

import commons.FileUtils;
import commons.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 implements Task<Integer> {
    FileUtils fileUtils = new FileUtils();
    String fileLines;

    public Day3() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day3/input.txt", "\n");
    }

    int convertStringToNumber(String numberInString) {
        return Integer.parseInt(numberInString.replaceAll("[^0-9]", ""));
    }

    @Override
    public Integer resolvePart1() {
        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)");
        Matcher matcher = pattern.matcher(fileLines);
        int result = 0;
        while (matcher.find()) {
            String[] numbers = matcher.group().split(",");
            result += convertStringToNumber(numbers[0]) * convertStringToNumber(numbers[1]);
        }

        return result;
    }

    @Override
    public Integer resolvePart2() {
        Pattern pattern = Pattern.compile("mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\)");
        Matcher matcher = pattern.matcher(fileLines);
        int result = 0;
        boolean multiplication = true;
        while (matcher.find()) {
            if (matcher.group().contains("do")) {
                multiplication = matcher.group().equals("do()");
            } else if (multiplication) {
                String[] numbers = matcher.group().split(",");
                result += convertStringToNumber(numbers[0]) * convertStringToNumber(numbers[1]);
            }
        }

        return result;
    }
}
