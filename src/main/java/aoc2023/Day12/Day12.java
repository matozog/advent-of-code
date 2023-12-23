package aoc2023.Day12;

import utils.FileUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;

    List<Spring> springs = new ArrayList<>();

    public Day12() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day12/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        Arrays.stream(fLines.split("\n")).forEach(line -> {
            String[] springData = line.split(" ");
            String[] damageGroups = springData[1].split(",");
            springs.add(new Spring(springData[0], damageGroups));
        });
    }

    int getPossibleArrangements(Spring spring) {
        Set<String> possibleArrangementsSet = new HashSet();
        int currentBrokenFields = Arrays.stream(spring.records).filter(record -> record.equals("#")).toArray().length;
        int unknownElements = Arrays.stream(spring.records).filter(record -> record.equals("?")).toArray().length;
        int sumOfDamagedElements = Arrays.stream(spring.damagedGroups).sum();
        int elementsToFit = sumOfDamagedElements - currentBrokenFields;
        List<Integer> unknownElementsIndexes = new ArrayList<>();
        for (int i = 0; i < spring.records.length; i++) {
            if (spring.records[i].equals("?")) {
                unknownElementsIndexes.add(i);
            }
        }
        String[] tmpRecord;

        int reverseCounter = elementsToFit - 1;
        for (int z = 0; z < (unknownElements - elementsToFit + 1); z++) {
            tmpRecord = spring.records.clone();
            for (int y = 0; y < elementsToFit; y++) {
                tmpRecord[unknownElementsIndexes.get(y + z)] = "#";
            }
            if (spring.checkIfArrangementIsPossible(tmpRecord)) {
                possibleArrangementsSet.add(String.join("", tmpRecord));
            }
            for (int j = reverseCounter + z; j > 0; j--) {
                for (int i = 0; i < unknownElements - elementsToFit - z; i++) {
                    tmpRecord[unknownElementsIndexes.get(j + i)] = "?";
                    tmpRecord[unknownElementsIndexes.get(j + i + 1)] = "#";
                    if (spring.checkIfArrangementIsPossible(tmpRecord)) {
                        possibleArrangementsSet.add(String.join("", tmpRecord));
                    }
                }
            }
        }

        for (int z = unknownElements - 1; z > elementsToFit - 1; z--) {
            tmpRecord = spring.records.clone();
            for (int y = 0; y < elementsToFit; y++) {
                tmpRecord[unknownElementsIndexes.get(z - y)] = "#";
            }
            if (spring.checkIfArrangementIsPossible(tmpRecord)) {
                possibleArrangementsSet.add(String.join("", tmpRecord));
            }
            for (int j = z - elementsToFit + 1; j < unknownElements; j++) {
                for (int i = 0; i < unknownElements - elementsToFit - (unknownElements - z - 1); i++) {
                    tmpRecord[unknownElementsIndexes.get(j - i)] = "?";
                    tmpRecord[unknownElementsIndexes.get(j - i - 1)] = "#";
                    if (spring.checkIfArrangementIsPossible(tmpRecord)) {
                        possibleArrangementsSet.add(String.join("", tmpRecord));
                    }
                }
            }
        }

        return possibleArrangementsSet.size();
    }

    public int resolvePart1() {
        int sum = 0;
        int possibleArrangements;
        for (Spring spring : springs) {
            possibleArrangements = getPossibleArrangements(spring);
            System.out.println(possibleArrangements);
            sum += possibleArrangements;
        }
        return sum;
    }
}

class Spring {
    String[] records;
    int[] damagedGroups;

    public Spring(String records, String[] damagedGroups) {
        this.records = records.split("");
        this.damagedGroups = Arrays.stream(damagedGroups).mapToInt(Integer::valueOf).toArray();
    }

    public boolean checkIfArrangementIsPossible(String[] record) {
        List<Integer> tmpDamagedGroups = new ArrayList<>();
        Pattern pattern = Pattern.compile("#+");
        Matcher matcher = pattern.matcher(String.join("", record));
        while (matcher.find()) {
            tmpDamagedGroups.add(matcher.group().length());
        }
        boolean isPossibleArrangement = false;
        if (Arrays.equals(this.damagedGroups, tmpDamagedGroups.stream().mapToInt(Integer::valueOf).toArray())) {
            isPossibleArrangement = true;
        }
        return isPossibleArrangement;
    }
}
