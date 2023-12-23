package day8;

import common.Task;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Day8 implements Task {

    FileUtils fileUtils;
    private ArrayList<int[]> treeGrid = new ArrayList<int[]>();
    private int amountOfRows, amountOfColumns;

    public Day8() {
        fileUtils = new FileUtils();

        String fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2022/day8/input.txt", "\n");
        fillGridTrees(fileLines);
    }

    private void fillGridTrees(String fileLines) {
        String[] linesArr = fileLines.split("\n");
        amountOfRows = linesArr.length;
        Arrays.stream(linesArr).forEach(line -> {
            treeGrid.add(line.chars().map(tree -> tree - '0').toArray());
            amountOfColumns = line.length();
        });
    }

    private boolean checkIfTreeIsVisible(int height, ArrayList<Integer> elementsBeforeEdge) {
        return elementsBeforeEdge.stream().allMatch(treeHeight -> treeHeight < height);
    }

    private ArrayList<Integer> getNeighboursVertical(int start, int stop, int columnIndex) {
        ArrayList<Integer> topNeighbours = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            topNeighbours.add(treeGrid.get(i)[columnIndex]);
        }
        return topNeighbours;
    }

    private ArrayList<Integer> getNeighboursHorizontal(int start, int stop, int rowIndex) {
        ArrayList<Integer> topNeighbours = new ArrayList<>();
        for (int i = start; i < stop; i++) {
            topNeighbours.add(treeGrid.get(rowIndex)[i]);
        }
        return topNeighbours;
    }

    private boolean isVisibleFromAnyDirection(int rowIndex, int columnIndex) {
        return checkIfTreeIsVisible(treeGrid.get(rowIndex)[columnIndex], getNeighboursVertical(0, rowIndex, columnIndex)) // TOP
                || checkIfTreeIsVisible(treeGrid.get(rowIndex)[columnIndex], getNeighboursVertical(rowIndex + 1, amountOfRows, columnIndex)) // BOTTOM
                || checkIfTreeIsVisible(treeGrid.get(rowIndex)[columnIndex], getNeighboursHorizontal(0, columnIndex, rowIndex)) // LEFT
                || checkIfTreeIsVisible(treeGrid.get(rowIndex)[columnIndex], getNeighboursHorizontal(columnIndex + 1, amountOfColumns, rowIndex)); // RIGHT
    }

    @Override
    public String resolvePart1() {
        int numberOfVisibleTrees = 0;

        for (int rowIndex = 0; rowIndex < treeGrid.size(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < treeGrid.get(0).length; columnIndex++) {
                if (isVisibleFromAnyDirection(rowIndex, columnIndex)) {
                    numberOfVisibleTrees++;
                }
            }
        }

        return String.valueOf(numberOfVisibleTrees);
    }

    private int getViewDistance(int treeHeight, ArrayList<Integer> neighbours) {
        if (neighbours.size() == 0) return 0;

        int distanceViewSize = 0;
        for (Integer neighbour : neighbours) {
            distanceViewSize++;
            if (neighbour >= treeHeight) {
                break;
            }
        }

        return distanceViewSize;
    }

    @Override
    public String resolvePart2() {
        int maxScenicScore = 0;
        for (int rowIndex = 0; rowIndex < treeGrid.size(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < treeGrid.get(0).length; columnIndex++) {
                // TOP
                ArrayList<Integer> topNeighbours = getNeighboursVertical(0, rowIndex, columnIndex);
                Collections.reverse(topNeighbours);
                int topDistanceView = getViewDistance(treeGrid.get(rowIndex)[columnIndex], topNeighbours);

                // BOTTOM
                ArrayList<Integer> bottomNeighbours = getNeighboursVertical(rowIndex + 1, amountOfRows, columnIndex);
                int bottomDistanceView = getViewDistance(treeGrid.get(rowIndex)[columnIndex], bottomNeighbours);

                // LEFT
                ArrayList<Integer> leftNeighbours = getNeighboursHorizontal(0, columnIndex, rowIndex);
                Collections.reverse(leftNeighbours);
                int leftDistanceView = getViewDistance(treeGrid.get(rowIndex)[columnIndex], leftNeighbours);

                // RIGHT
                ArrayList<Integer> rightNeighbours = getNeighboursHorizontal(columnIndex + 1, amountOfColumns, rowIndex);
                int rightDistanceView = getViewDistance(treeGrid.get(rowIndex)[columnIndex], rightNeighbours);

                int treeScenicScore = topDistanceView * bottomDistanceView * leftDistanceView * rightDistanceView;

                if (treeScenicScore > maxScenicScore) maxScenicScore = treeScenicScore;

            }
        }

        return String.valueOf(maxScenicScore);
    }
}
