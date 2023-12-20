package Day17;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

public class Day17 {
    FileUtils fileUtils = new FileUtils();
    String fileLines;

    int[][] map;

    public Day17() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2023/day17/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        String[] lines = fLines.split("\n");
        map = new int[lines.length][];
        for (int i = 0; i < map.length; i++) {
            map[i] = Arrays.stream(lines[i].split("")).mapToInt(Integer::valueOf).toArray();
        }
    }

    List<List<Node>> findPath(Node node, List<List<Node>> currentPath, int forwardCounter) {
        if (node.y == map.length - 1 && node.x == map[0].length - 1) {
            return currentPath;
        }
        if (currentPath.contains(node) || node.x < 0 || node.y < 0 || node.x >= map[0].length || node.y >= map.length) {
            return new ArrayList<>();
        }

        for (List<Node> nodes : currentPath) {
            nodes.add(node);
        }

        Node leftNode, rightNode, forwardNode;

        if (node.direction == Direction.UP) {
            leftNode = new Node(node.x - 1, node.y, Direction.LEFT);
            rightNode = new Node(node.x + 1, node.y, Direction.RIGHT);
            forwardNode = new Node(node.x, node.y - 1, Direction.UP);

        } else if (node.direction == Direction.DOWN) {
            leftNode = new Node(node.x + 1, node.y, Direction.RIGHT);
            rightNode = new Node(node.x - 1, node.y, Direction.LEFT);
            forwardNode = new Node(node.x, node.y + 1, Direction.DOWN);
        } else if (node.direction == Direction.RIGHT) {
            leftNode = new Node(node.x, node.y - 1, Direction.UP);
            rightNode = new Node(node.x, node.y + 1, Direction.DOWN);
            forwardNode = new Node(node.x + 1, node.y, Direction.RIGHT);
        } else {
            leftNode = new Node(node.x, node.y + 1, Direction.DOWN);
            rightNode = new Node(node.x, node.y - 1, Direction.UP);
            forwardNode = new Node(node.x - 1, node.y, Direction.LEFT);
        }

        List<List<Node>> pathsList = new ArrayList<List<Node>>();
        pathsList = Stream.concat(pathsList.stream(), findPath(leftNode, currentPath, 0).stream()).toList();
        pathsList = Stream.concat(pathsList.stream(), findPath(rightNode, currentPath, 0).stream()).toList();
        if (forwardCounter <= 3)
            pathsList = Stream.concat(pathsList.stream(), findPath(forwardNode, currentPath, forwardCounter).stream()).toList();

        return pathsList;
    }

    public int resolvePart1() {
        Node startNode = new Node(0, 0, Direction.RIGHT);
        List<List<Node>> currentPath = new ArrayList<>();
        List<List<Node>> path = findPath(startNode, currentPath, 0);
        return 0;
    }
}

class Node {
    int x, y;
    Direction direction;

    public Node(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
}
