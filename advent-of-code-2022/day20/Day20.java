package day20;

import common.Task;
import lombok.Getter;
import lombok.Setter;
import utils.FileUtils;

import java.math.BigInteger;
import java.util.ArrayList;

public class Day20 implements Task {
    FileUtils fileUtils;

    ArrayList<Node> originalArray;
    ArrayList<Node> nodes = new ArrayList<>();

    BigInteger multiplier = new BigInteger("811589153");

    public Day20() {
        fileUtils = new FileUtils();

        String fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2022/day20/input.txt", "\n");
        fillArraysWithInputData(fileLines);
    }

    private void fillArraysWithInputData(String fileLines) {
        String[] linesArr = fileLines.split("\n");

        originalArray = new ArrayList<Node>();

        for (String s : linesArr) {
            Node newNode = new Node(new BigInteger(s));
            nodes.add(newNode);
            originalArray.add(newNode);
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
        int amountOfMoves = currentNode.getValue().abs().mod(new BigInteger(String.valueOf(nodes.size() - 1))).intValue();
        if (amountOfMoves == 0) return;

        Node tmpPrevNode = currentNode.getPreviousNeighbour();
        Node tmpNextNode = currentNode.getNextNeighbour();

        tmpPrevNode.setNextNeighbour(tmpNextNode);
        tmpNextNode.setPreviousNeighbour(tmpPrevNode);

        Node tmpNode = currentNode;
        if (tmpNode.getValue().signum() > 0) {
            for (int i = 0; i < amountOfMoves; i++) {
                tmpNode = tmpNode.getNextNeighbour();
            }

            currentNode.setPreviousNeighbour(tmpNode);
            currentNode.setNextNeighbour(tmpNode.getNextNeighbour());
            tmpNode.getNextNeighbour().setPreviousNeighbour(currentNode);
            tmpNode.setNextNeighbour(currentNode);
        } else if (tmpNode.getValue().signum() < 0) {
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
    public String resolvePart1() {
        for (int index = 0; index < originalArray.size(); index++) {
            int finalIndex = index;
            mixNode(nodes.stream().filter(node -> node == originalArray.get(finalIndex)).findFirst().orElseThrow());
        }
        Node nodeWithZero = nodes.stream().filter(node -> node.getValue().signum() == 0).findFirst().orElseThrow();

        Node firstCoordinate = findNeighbour(nodeWithZero, 1000);
        Node secondCoordinate = findNeighbour(nodeWithZero, 2000);
        Node thirdCoordinate = findNeighbour(nodeWithZero, 3000);

        return firstCoordinate.getValue().add(secondCoordinate.getValue()).add(thirdCoordinate.getValue()).toString();
    }

    void multiplyAllElementsByMultiplier() {
        for (Node node : originalArray) {
            node.setValue(node.getValue().multiply(multiplier));
        }
    }

    @Override
    public String resolvePart2() {
        multiplyAllElementsByMultiplier();
        for (int i = 0; i < 10; i++) {
            for (int index = 0; index < originalArray.size(); index++) {
                int finalIndex = index;
                mixNode(nodes.stream().filter(node -> node == originalArray.get(finalIndex)).findFirst().orElseThrow());
            }
        }
        Node nodeWithZero = nodes.stream().filter(node -> node.getValue().signum() == 0).findFirst().orElseThrow();

        Node firstCoordinate = findNeighbour(nodeWithZero, 1000);
        Node secondCoordinate = findNeighbour(nodeWithZero, 2000);
        Node thirdCoordinate = findNeighbour(nodeWithZero, 3000);

        return firstCoordinate.getValue().add(secondCoordinate.getValue()).add(thirdCoordinate.getValue()).toString();
    }
}

@Setter
@Getter
class Node{
    private Node nextNeighbour;
    private Node previousNeighbour;
    private BigInteger value;

    public Node(BigInteger value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
