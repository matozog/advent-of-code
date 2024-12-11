package aoc2024.day10;

import commons.FileUtils;
import commons.Task;
import lombok.EqualsAndHashCode;

import java.util.*;

public class Day10 implements Task<Integer> {
    String fileLine;
    Map<String, Node> nodes = new HashMap<>();

    public String generateKey(int rowIndex, int columnIndex) {
        return rowIndex + "_" + columnIndex;
    }

    public Day10() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day10/input.txt", "\n");
        String[] lines = fileLine.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String[] heights = lines[i].split("");
            for (int j = 0; j < heights.length; j++) {
                if (!heights[j].equals("."))
                    nodes.put(generateKey(i, j), new Node(i, j, Integer.parseInt(heights[j])));
            }
        }

        nodes.keySet().forEach(key -> {
            Node node = nodes.get(key);
            Node topNode = nodes.get(generateKey(node.rowIndex - 1, node.columnIndex));
            if (topNode != null) node.neighbours.add(topNode);
            Node bottomNode = nodes.get(generateKey(node.rowIndex + 1, node.columnIndex));
            if (bottomNode != null) node.neighbours.add(bottomNode);
            Node leftNode = nodes.get(generateKey(node.rowIndex, node.columnIndex - 1));
            if (leftNode != null) node.neighbours.add(leftNode);
            Node rightNode = nodes.get(generateKey(node.rowIndex, node.columnIndex + 1));
            if (rightNode != null) node.neighbours.add(rightNode);
        });
    }

    Node[] getHikingTrails(Node node, int steps) {
        if (node.value == 9) return new Node[]{node};
        if (steps > 9) return new Node[]{};

        List<Node> trails = node.neighbours.stream()
                .filter(neighbour -> neighbour.value == node.value + 1)
                .map(neighbour -> getHikingTrails(neighbour, steps + 1))
                .filter(arr -> arr.length > 0)
                .flatMap(Arrays::stream)
                .toList();

        return trails.toArray(new Node[]{});
    }

    @Override
    public Integer resolvePart1() {
        List<Node> trails = nodes.keySet()
                .stream()
                .filter(key -> nodes.get(key).value == 0)
                .map(key -> getHikingTrails(nodes.get(key), 0))
                .map(arr -> new HashSet<>(Arrays.asList(arr))).flatMap(Set::stream).toList();
        return trails.size();
    }

    @Override
    public Integer resolvePart2() {
        List<Node> trails = nodes.keySet()
                .stream()
                .filter(key -> nodes.get(key).value == 0)
                .map(key -> getHikingTrails(nodes.get(key), 0))
                .flatMap(Arrays::stream).toList();
        return trails.size();
    }
}

@EqualsAndHashCode
class Node {
    int rowIndex, columnIndex;
    @EqualsAndHashCode.Exclude
    List<Node> neighbours = new ArrayList<>();
    int value;

    public Node(int rowIndex, int columnIndex, int value) {
        this.value = value;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }
}
