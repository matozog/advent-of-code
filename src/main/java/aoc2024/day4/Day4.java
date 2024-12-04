package aoc2024.day4;

import commons.FileUtils;
import commons.Task;

public class Day4 implements Task<Integer> {

    FileUtils fileUtils = new FileUtils();
    String fileLine;
    char[][] puzzles;
    String wordToFind = "XMAS";

    public Day4() {
        fileLine = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day4/input.txt", "\n");
        String[] lines = fileLine.split("\n");
        puzzles = new char[lines.length][lines[0].length()];

        for (int i = 0; i < lines.length; i++) {
            puzzles[i] = lines[i].toCharArray();
        }
    }

    int checkVertically(int row, int column) {
        StringBuilder firstWord = new StringBuilder();
        StringBuilder secondWord = new StringBuilder();
        int result = 0;

        for (int i = 0; i < wordToFind.length(); i++) {
            if (row - i >= 0)
                firstWord.append(puzzles[row - i][column]);
            if (row + i < puzzles.length)
                secondWord.append(puzzles[row + i][column]);
        }

        if (firstWord.toString().equals(wordToFind)) result++;
        if (secondWord.toString().equals(wordToFind)) result++;

        return result;
    }

    int checkHorizontally(int row, int column) {
        StringBuilder firstWord = new StringBuilder();
        StringBuilder secondWord = new StringBuilder();
        int result = 0;

        for (int i = 0; i < wordToFind.length(); i++) {
            if (column - i >= 0)
                firstWord.append(puzzles[row][column - i]);
            if (column + i < puzzles[row].length)
                secondWord.append(puzzles[row][column + i]);
        }

        if (firstWord.toString().equals(wordToFind)) result++;
        if (secondWord.toString().equals(wordToFind)) result++;

        return result;
    }

    int checkDiagonally(int row, int column) {
        StringBuilder firstWord = new StringBuilder();
        StringBuilder secondWord = new StringBuilder();
        StringBuilder thirdWord = new StringBuilder();
        StringBuilder fourthWord = new StringBuilder();
        int result = 0;

        for (int i = 0; i < wordToFind.length(); i++) {
            if (row - i >= 0 && column - i >= 0)
                firstWord.append(puzzles[row - i][column - i]);
            if (row - i >= 0 && column + i < puzzles[row].length)
                secondWord.append(puzzles[row - i][column + i]);
            if (row + i < puzzles.length && column - i >= 0)
                thirdWord.append(puzzles[row + i][column - i]);
            if (row + i < puzzles.length && column + i < puzzles[row].length)
                fourthWord.append(puzzles[row + i][column + i]);
        }

        if (firstWord.toString().equals(wordToFind)) result++;
        if (secondWord.toString().equals(wordToFind)) result++;
        if (thirdWord.toString().equals(wordToFind)) result++;
        if (fourthWord.toString().equals(wordToFind)) result++;

        return result;
    }

    @Override
    public Integer resolvePart1() {
        int result = 0;
        for (int i = 0; i < puzzles.length; i++) {
            for (int j = 0; j < puzzles[0].length; j++) {
                if (puzzles[i][j] == 'X') {
                    result += checkVertically(i, j);
                    result += checkHorizontally(i, j);
                    result += checkDiagonally(i, j);
                }
            }
        }
        return result;
    }

    int checkXmas(int row, int column) {
        if (row == 0 || row == puzzles.length - 1 || column == 0 || column == puzzles[row].length - 1) return 0;

        StringBuilder firstWord = new StringBuilder();
        StringBuilder secondWord = new StringBuilder();

        firstWord.append(puzzles[row - 1][column - 1]);
        firstWord.append(puzzles[row][column]);
        firstWord.append(puzzles[row + 1][column + 1]);

        secondWord.append(puzzles[row + 1][column - 1]);
        secondWord.append(puzzles[row][column]);
        secondWord.append(puzzles[row - 1][column + 1]);

        if ((firstWord.toString().equals("MAS") || firstWord.toString().equals("SAM")) && (secondWord.toString()
                .equals("SAM") || secondWord.toString().equals("MAS")))
            return 1;

        return 0;
    }

    @Override
    public Integer resolvePart2() {
        int result = 0;
        for (int i = 0; i < puzzles.length; i++) {
            for (int j = 0; j < puzzles[0].length; j++) {
                if (puzzles[i][j] == 'A') {
                    result += checkXmas(i, j);
                }
            }
        }
        return result;
    }
}
