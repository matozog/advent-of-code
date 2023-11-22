package day20;

import common.Task;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class Day20index implements Task {
    FileUtils fileUtils;

    ArrayList<Integer> originalArray;
    ArrayList<Integer> decryptedArray;

    public Day20index() {
        fileUtils = new FileUtils();

        String fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2022/day20/input.txt", "\n");
        fillArraysWithInputData(fileLines);
    }

    private void fillArraysWithInputData(String fileLines) {
        String[] linesArr = fileLines.split("\n");

        originalArray = new ArrayList<Integer>(Arrays.stream(linesArr).map(Integer::parseInt).toList());
        decryptedArray = new ArrayList<>(originalArray);
    }

    private Integer calculateNewElementIndex(Integer currentIndex, Integer indexToSwap, int smallerArrFactor) {
        return ((currentIndex + indexToSwap) % (decryptedArray.size() + smallerArrFactor));
    }

    private void swapMixedElements(Integer currentIndex, Integer value) {
        if (value != 0) {
            Integer newIndex = calculateNewElementIndex(currentIndex, value, -1);
            if (newIndex <= 0) newIndex = decryptedArray.size() - 1 + newIndex;
            decryptedArray.remove((int) currentIndex);
            decryptedArray.add(newIndex, value);
        }
    }

    @Override
    public int resolvePart1() {
        for (int index = 0; index < originalArray.size(); index++) {
            swapMixedElements(decryptedArray.indexOf(originalArray.get(index)), originalArray.get(index));
        }

        int firstCoordinate = calculateNewElementIndex(decryptedArray.indexOf(0), 1000, 0);
        int secondCoordinate = calculateNewElementIndex(decryptedArray.indexOf(0), 2000, 0);
        int thirdCoordinate = calculateNewElementIndex(decryptedArray.indexOf(0), 3000, 0);

        System.out.println(decryptedArray.get(firstCoordinate));
        System.out.println(decryptedArray.get(secondCoordinate));
        System.out.println(decryptedArray.get(thirdCoordinate));
        System.out.println("Zero index " + decryptedArray.indexOf(0));

        return decryptedArray.get(firstCoordinate) + decryptedArray.get(secondCoordinate) + decryptedArray.get(thirdCoordinate);
    }

    @Override
    public int resolvePart2() {
        return 0;
    }
}
