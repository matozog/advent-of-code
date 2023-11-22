package day20;

import common.Task;
import lombok.Getter;
import lombok.Setter;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class Day20 implements Task {
    FileUtils fileUtils;

    ArrayList<Integer> originalArray;
    ArrayList<Node> nodes = new ArrayList<>();

    public Day20() {
        fileUtils = new FileUtils();

        String fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2022/day20/input.txt", "\n");
        fillArraysWithInputData(fileLines);
    }

    private void fillArraysWithInputData(String fileLines) {
        String[] linesArr = fileLines.split("\n");

        originalArray = new ArrayList<Integer>(Arrays.stream(linesArr).map(Integer::parseInt).toList());

        for (String s : linesArr) {
            Node newNode = new Node(Integer.parseInt(s));
            nodes.add(newNode);
        }

        for (int i = 0; i < nodes.size(); i++) {
            if (i == 0) {
                nodes.get(i).setNextNeighbour(nodes.get(i + 1));
                nodes.get(i).setPreviousNeighbour(nodes.get(nodes.size() - 1));
            } else if (i == nodes.size() - 1) {
                nodes.get(i).setPreviousNeighbour(nodes.get(i - 1));
                nodes.get(i).setNextNeighbour(nodes.get(0));
            } else {
                nodes.get(i).setNextNeighbour(nodes.get(i + 1));
                nodes.get(i).setPreviousNeighbour(nodes.get(i - 1));
            }
        }
    }

    private void mixNode(Node currentNode) {
        int amountOfMoves = (Math.abs(currentNode.getValue()) % (nodes.size() - 1));
        if (amountOfMoves == 0) return;

        Node tmpPrevNode = currentNode.getPreviousNeighbour();
        Node tmpNextNode = currentNode.getNextNeighbour();

        tmpPrevNode.setNextNeighbour(tmpNextNode);
        tmpNextNode.setPreviousNeighbour(tmpPrevNode);

        Node tmpNode = currentNode;
        if (tmpNode.getValue() > 0) {
            for (int i = 0; i < amountOfMoves; i++) {
                tmpNode = tmpNode.getNextNeighbour();
            }

            currentNode.setPreviousNeighbour(tmpNode);
            currentNode.setNextNeighbour(tmpNode.getNextNeighbour());
            tmpNode.getNextNeighbour().setPreviousNeighbour(currentNode);
            tmpNode.setNextNeighbour(currentNode);
        } else if (tmpNode.getValue() < 0) {
            for (int i = 0; i < amountOfMoves; i++) {
                tmpNode = tmpNode.getPreviousNeighbour();
            }

            currentNode.setPreviousNeighbour(tmpNode.getPreviousNeighbour());
            currentNode.setNextNeighbour(tmpNode);
            tmpNode.getPreviousNeighbour().setNextNeighbour(currentNode);
            tmpNode.setPreviousNeighbour(currentNode);
        }
    }


    private void printNodesAsArray() {
        Node tmpNode = nodes.get(0);
        do {
            System.out.print(tmpNode + " ");
            tmpNode = tmpNode.getNextNeighbour();
        } while (tmpNode.getValue() != nodes.get(0).getValue());
        System.out.println("");
    }

    private Node findNeighbour(Node currentNode, int distance) {
        Node tmpNode = currentNode;

        for (int i = 0; i < distance; i++) {
            tmpNode = tmpNode.getNextNeighbour();
        }

        return tmpNode;
    }

    @Override
    public int resolvePart1() {
        for (int index = 0; index < originalArray.size(); index++) {
            int finalIndex = index;
//            printNodesAsArray();
            mixNode(nodes.stream().filter(node -> node.getValue() == originalArray.get(finalIndex)).findFirst().orElseThrow());
        }
        printNodesAsArray();
        Node nodeWithZero = nodes.stream().filter(node -> node.getValue() == 0).findFirst().orElseThrow();

        Node firstCoordinate = findNeighbour(nodeWithZero, 1000);
        Node secondCoordinate = findNeighbour(nodeWithZero, 2000);
        Node thirdCoordinate = findNeighbour(nodeWithZero, 3000);

        System.out.println(firstCoordinate);
        System.out.println(secondCoordinate);
        System.out.println(thirdCoordinate);

        return firstCoordinate.getValue() + secondCoordinate.getValue() + thirdCoordinate.getValue();
    }

    @Override
    public int resolvePart2() {
        return 0;
    }
}

@Setter
@Getter
class Node {
    private Node nextNeighbour;
    private Node previousNeighbour;
    private int value;

    public Node(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
