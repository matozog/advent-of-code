package aoc2023.Day4;

import aoc2023.utils.Utils;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Day4 {
    FileUtils fileUtils = new FileUtils();
    String fileLines;
    ArrayList<Game> games = new ArrayList<Game>();

    public Day4() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day4/input.txt", "\n");

        fillDataStructure(fileLines);
    }

    Integer[] parseLineToIntArray(String line) {
        return Arrays.stream(line.trim().split("\\s+"))
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
    }

    void fillDataStructure(String lines) {
        AtomicInteger counter = new AtomicInteger(1);
        Arrays.stream(lines.split("\n"))
                .forEach(line -> {
                    String[] gameInfo = line.split("[:|]");
                    Integer[] winningArray = parseLineToIntArray(gameInfo[1]);
                    Integer[] numArray = parseLineToIntArray(gameInfo[2]);
                    games.add(new Game(winningArray, numArray, counter.get(), findAmountOfSameNumbers(numArray, winningArray)));
                    counter.set(counter.get() + 1);
                });
    }

    int findAmountOfSameNumbers(Integer[] arr1, Integer[] arr2) {
        return Arrays.stream(arr1)
                .filter(n1 -> Arrays.stream(arr2).anyMatch(n2 -> n2 == n1))
                .toArray().length;
    }

    public int resolvePart1() {
        return games.stream().map(game -> game.power)
                .reduce(0, (subtotal, element) -> subtotal + Utils.IntegerPower(2, element - 1));
    }

    public int resolvePart2() {
        for (int i = 0; i < games.size(); i++) {
            int lastIndex = i + games.get(i).power >= games.size() ? games.size() - 1 : i + games.get(i).power;
            for (int j = i + 1; j <= lastIndex; j++) {
                games.get(j).copies += games.get(i).copies;
            }
        }
        return games.stream().map(game -> game.copies).reduce(0, Integer::sum);
    }
}

class Game {
    Integer[] winningNumbers, cardNumbers;
    int id, power = 0;
    int copies = 1;

    public Game(Integer[] winningNumbers, Integer[] cardNumbers, int id, int power) {
        this.cardNumbers = cardNumbers;
        this.winningNumbers = winningNumbers;
        this.id = id;
        this.power = power;
    }
}
