package aoc2023.Day8;

import utils.FileUtils;
import utils.Utils;

import java.math.BigInteger;
import java.util.*;

public class Day8 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;
    String sequence;
    HashMap<String, String[]> nodeMap = new HashMap<>();
    List<String> nodesEndsWithA = new ArrayList<>();
    List<String> nodesEndsWithZ = new ArrayList<>();

    public Day8() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day8/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fileLines) {
        String[] lines = fileLines.split("\n");
        sequence = lines[0];
        Arrays.stream(lines).skip(2).forEach(line -> {
            if (line.isEmpty()) return;
            String[] nodeData = line.split(" = ");
            String name = nodeData[0].trim();

            if (name.endsWith("A")) {
                nodesEndsWithA.add(name);
            }
            if (name.endsWith("Z")) {
                nodesEndsWithZ.add(name);
            }

            String[] neighbourData = nodeData[1].split(", ");

            nodeMap.put(name, new String[]{neighbourData[0].replaceAll("\\(", ""), neighbourData[1].replaceAll("\\)", "")});
        });
    }

    long findTotalStepsToReachNode(String startNode, List<String> endNodes) {
        if (!nodeMap.containsKey(startNode)) return 0;
        String currentNode = startNode;
        char[] sequenceChars = sequence.toCharArray();
        int counter = 0;
        long totalSteps = 0;
        while (endNodes.stream().noneMatch(currentNode::equals)) {
            currentNode = sequenceChars[counter] == 'R' ? nodeMap.get(currentNode)[1] : nodeMap.get(currentNode)[0];
            counter++;
            if (counter == sequenceChars.length) {
                totalSteps += counter;
                counter = 0;
            }
        }

        totalSteps += counter;
        return totalSteps;
    }

    public long resolvePart1() {
        return findTotalStepsToReachNode("AAA", new ArrayList<>() {{
            add("ZZZ");
        }});
    }

    public long resolvePart2() {
        return nodesEndsWithA.stream()
                .map(nodeWithA -> findTotalStepsToReachNode(nodeWithA, nodesEndsWithZ))
                .reduce(1L, (acc, val) ->
                        Long.parseLong(Utils.lcm(BigInteger.valueOf(val), BigInteger.valueOf(acc)).toString()));
    }

}
