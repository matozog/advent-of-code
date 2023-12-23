package aoc2023.Day6;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;
    ArrayList<Race> races = new ArrayList<>();

    public Day6() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day6/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        String[] lines = fLines.split("\n");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcherTime = pattern.matcher(lines[0]);
        Matcher matcherDistance = pattern.matcher(lines[1]);
        while (matcherTime.find()) {
            if (matcherDistance.find()) {
                races.add(new Race(matcherTime.group(), matcherDistance.group()));
            }
        }
    }

    long calculateDistance(long time, long pressedTime) {
        long timeToMove = time - pressedTime;
        return timeToMove * pressedTime;
    }

    int findWinningOptions(long time, long distance) {
        int winningOption = 0;
        for (long i = 1; i < time; i++) {
            if (calculateDistance(time, i) > distance) winningOption++;
        }
        return winningOption;
    }

    public long resolvePart1() {
        long[] racesWinningOption = new long[races.size()];
        int counter = 0;
        for (Race race : races) {
            racesWinningOption[counter] = findWinningOptions(race.time, race.distance);
            counter++;
        }

        return Arrays.stream(racesWinningOption).reduce(1, (a, b) -> a * b);
    }

    public long resolvePart2() {
        long raceTime = Long.parseLong(Arrays.stream(races
                        .stream()
                        .map(race -> race.time)
                        .toArray())
                .reduce("", (a, b) -> String.valueOf(a) + String.valueOf(b))
                .toString());

        long raceDistance = Long.parseLong(Arrays.stream(races
                        .stream()
                        .map(race -> race.distance)
                        .toArray())
                .reduce("", (a, b) -> String.valueOf(a) + String.valueOf(b))
                .toString());

        return findWinningOptions(raceTime, raceDistance);
    }

}

class Race {
    long time, distance;

    public Race(String time, String distance) {
        this.time = Long.parseLong(time);
        this.distance = Long.parseLong(distance);
    }
}
