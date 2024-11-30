package aoc2023.day2;

import commons.FileUtils;
import commons.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 implements Task {

    FileUtils fileUtils = new FileUtils();
    String fileLines;
    ArrayList<Game> gamesList = new ArrayList<Game>();

    public Day2() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day2/input.txt", "\n");

        parseInputToGameObjects();
    }

    void setCubeValueFromStringToSet(Set set, String cubeValue) {
        Pattern patter = Pattern.compile("[0-9]+");
        Matcher matcher = patter.matcher(cubeValue);
        if (!matcher.find()) return;
        int amountOfCubes = Integer.parseInt(matcher.group());
        if (cubeValue.contains("blue")) {
            set.numberOfBlueCubes = amountOfCubes;
        } else if (cubeValue.contains("red")) {
            set.numberOfRedCubes = amountOfCubes;
        } else if (cubeValue.contains("green")) {
            set.numberOfGreenCubes = amountOfCubes;
        }
    }

    ArrayList<Set> parseLineToGameSet(String line) {
        ArrayList<Set> gameSets = new ArrayList<>();
        Pattern pattern = Pattern.compile("[0-9]+ blue|[0-9]+ green|[0-9]+ red");
        Arrays.stream(line.split("[;]")).forEach(lineSet -> {
            Set gameSet = new Set();
            Matcher matcher = pattern.matcher(lineSet);
            while (matcher.find()) {
                setCubeValueFromStringToSet(gameSet, matcher.group());
            }
            gameSets.add(gameSet);
        });

        return gameSets;
    }

    void parseInputToGameObjects() {
        AtomicInteger gameId = new AtomicInteger(1);
        Arrays.stream(fileLines.split("\n")).forEach(line -> {
            Game game = new Game();
            game.setsList = parseLineToGameSet(line);
            gamesList.add(game);
            game.Id = gameId.get();
            gameId.getAndIncrement();
        });
    }

    boolean isSetPossibleToPlay(Set set, int red, int green, int blue) {
        return set.numberOfBlueCubes <= blue && set.numberOfGreenCubes <= green && set.numberOfRedCubes <= red;
    }

    public Integer resolvePart1() {
        int amountOfRedCubes = 12, amountOfBlueCubes = 14, amountOfGreenCubes = 13;
        return gamesList.stream()
                .filter(game -> game.setsList.stream()
                        .allMatch(set -> isSetPossibleToPlay(set, amountOfRedCubes, amountOfGreenCubes, amountOfBlueCubes)))
                .map(game -> game.Id)
                .reduce(0, Integer::sum);
    }

    public Integer resolvePart2() {
        gamesList.forEach(game -> {
            game.setsList.forEach(set -> {
                if (set.numberOfGreenCubes > game.minAmountOfGreenCubes)
                    game.minAmountOfGreenCubes = set.numberOfGreenCubes;
                if (set.numberOfRedCubes > game.minAmountOfRedCubes) game.minAmountOfRedCubes = set.numberOfRedCubes;
                if (set.numberOfBlueCubes > game.minAmountOfBlueCubes)
                    game.minAmountOfBlueCubes = set.numberOfBlueCubes;
            });
        });
        return gamesList.stream()
                .map(game -> game.minAmountOfBlueCubes * game.minAmountOfGreenCubes * game.minAmountOfRedCubes)
                .reduce(0, Integer::sum);
    }
}

class Game {
    int Id;
    int minAmountOfGreenCubes = 0, minAmountOfBlueCubes = 0, minAmountOfRedCubes = 0;

    ArrayList<Set> setsList = new ArrayList<>();
}

class Set {
    int numberOfGreenCubes = 0, numberOfBlueCubes = 0, numberOfRedCubes = 0;
}
