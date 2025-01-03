package aoc2023.day1;

import aoc2023.constants.Constants;
import commons.FileUtils;
import commons.Task;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 implements Task {

    FileUtils fileUtils = new FileUtils();
    String fileLines;

    public Day1 () {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day1/input.txt", "\n");
    }

    public Integer resolvePart1() {
        Pattern pattern = Pattern.compile("\\d");
        return Arrays.stream(fileLines.split("\n")).map(line -> {
            Matcher matcher = pattern.matcher(line);
            int firstElement, lastElement = -1;

            if(!matcher.find()) return 0;

            firstElement = Integer.parseInt(matcher.group(0));
            while (matcher.find()) {
                lastElement = Integer.parseInt(matcher.group(0));
            }

            if (lastElement < 0) lastElement = firstElement;

            return firstElement * 10 + lastElement;
        }).reduce(0, Integer::sum);
    }

    private int convertStringNumberToInt(String numberInString) {
        return Constants.mappedNumbers.get(numberInString);
    }

    private int parseMatchToInt (String match) {
        return match.length() > 1 ? convertStringNumberToInt(match) : Integer.parseInt(match);
    }

    public Integer resolvePart2() {
        Pattern pattern = Pattern.compile("one|two|three|four|five|six|seven|eight|nine|\\d");
        return Arrays.stream(fileLines.split("\n")).map(line -> {
            Matcher matcher = pattern.matcher(line);
            int firstElement, lastElement = -1, startIndex;

            if(!matcher.find()) return 0;

            startIndex = matcher.end();
            firstElement = parseMatchToInt(matcher.group(0));
            while (matcher.find(startIndex - matcher.group(0).length() + 1)) {
                lastElement = parseMatchToInt(matcher.group(0));
                startIndex = matcher.end();
            }

            if (lastElement < 0) lastElement = firstElement;

            return firstElement * 10 + lastElement;
        }).reduce(0, Integer::sum);
    }
}
