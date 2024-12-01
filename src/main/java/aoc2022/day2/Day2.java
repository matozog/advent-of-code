package aoc2022.day2;

import commons.FileUtils;
import commons.Pair;
import commons.Task;

import java.util.ArrayList;
import java.util.Arrays;

public class Day2 implements Task<Integer> {

    FileUtils fileUtils = new FileUtils();
    String fileLines;
    ArrayList<Pair<OpponentCommand, PlayerCommand>> rounds = new ArrayList<>();

    public Day2() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2022/day2/input.txt", "\n");
        Arrays.stream(fileLines.split("\n")).forEach(line -> {
            String[] commands = line.split(" ");
            rounds.add(new Pair<>(OpponentCommand.valueOf(commands[0]), PlayerCommand.valueOf(commands[1])));
        });
    }

    int calculateRoundResult(Pair<OpponentCommand, PlayerCommand> round) {
        int result = round.getValue().ordinal() + 1;

        switch (round.getValue()) {
            case X -> {
                if (round.getKey() == OpponentCommand.C) {
                    result += 6;
                } else if (round.getKey() == OpponentCommand.A) {
                    result += 3;
                }
            }
            case Y -> {
                if (round.getKey() == OpponentCommand.A) {
                    result += 6;
                } else if (round.getKey() == OpponentCommand.B) {
                    result += 3;
                }
            }
            case Z -> {
                if (round.getKey() == OpponentCommand.B) {
                    result += 6;
                } else if (round.getKey() == OpponentCommand.C) {
                    result += 3;
                }
            }
        }

        return result;
    }

    @Override
    public Integer resolvePart1() {
        return rounds.stream().map(this::calculateRoundResult).reduce(0, Integer::sum);
    }


    @Override
    public Integer resolvePart2() {
        return rounds.stream()
                .map(round -> new Pair<>(round.getKey(), round.getValue().getCommandByWinningStatus(round.getKey())))
                .map(this::calculateRoundResult).reduce(0, Integer::sum);
    }
}


enum OpponentCommand {
    A,
    B,
    C;
}

enum PlayerCommand {
    X,
    Y,
    Z;

    PlayerCommand getCommandByWinningStatus(OpponentCommand opponentCommand) {
        return switch (this) {
            case X ->
                    PlayerCommand.values()[opponentCommand.ordinal() == 0 ? 2 : opponentCommand.ordinal() - 1];// opponentCommand.getLoseStatus();
            case Y -> PlayerCommand.values()[opponentCommand.ordinal()];
            case Z -> PlayerCommand.values()[opponentCommand.ordinal() == 2 ? 0 : opponentCommand.ordinal() + 1];
        };
    }
}