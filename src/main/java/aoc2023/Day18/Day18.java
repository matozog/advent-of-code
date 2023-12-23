package aoc2023.Day18;

import utils.FileUtils;
import utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

enum Direction {
    U,
    D,
    L,
    R
}

public class Day18 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;

    List<Field> points = new ArrayList<>();
    List<DigInstruction> digInstructionList = new ArrayList<>();
    int maxX, maxY, minX, minY;

    public Day18() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day18/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        String[] lines = fLines.split("\n");
        for (String line : lines) {
            String[] digData = line.split(" ");
            digInstructionList.add(new DigInstruction(Direction.valueOf(digData[0]), Integer.parseInt(digData[1]), digData[2]));
        }
    }

    void createPointsList() {
        int lastX = 0, lastY = 0;
        Direction lastDirection = Direction.U;
        Direction tmpInterior = Direction.R;
        for (DigInstruction digInstruction : digInstructionList) {

            switch (digInstruction.direction) {
                case U -> {
                    if ((lastDirection == Direction.R && tmpInterior == Direction.D) || (lastDirection == Direction.L && tmpInterior == Direction.U)) {
                        tmpInterior = Direction.R;
                        points.get(points.size() - 1).interior.add(tmpInterior);
                    } else {
                        if (lastDirection == Direction.L && tmpInterior == Direction.D) {
                            points.get(points.size() - 1).interior.add(Direction.L);
                        }
                        tmpInterior = Direction.L;
                    }
                }
                case D -> {
                    if ((lastDirection == Direction.R && tmpInterior == Direction.U) || (lastDirection == Direction.L && tmpInterior == Direction.D)) {
                        tmpInterior = Direction.R;
                        points.get(points.size() - 1).interior.add(tmpInterior);
                    } else {
                        if (lastDirection == Direction.L && tmpInterior == Direction.U) {
                            points.get(points.size() - 1).interior.add(Direction.L);
                        }
                        tmpInterior = Direction.L;

                    }
                }
                case R -> {
                    if ((lastDirection == Direction.U && tmpInterior == Direction.R) || (lastDirection == Direction.D && tmpInterior == Direction.L)) {
                        tmpInterior = Direction.D;
                        if (points.size() > 0) points.get(points.size() - 1).interior.add(tmpInterior);

                    } else {
                        if (lastDirection == Direction.U && tmpInterior == Direction.L) {
                            points.get(points.size() - 1).interior.add(Direction.U);
                        }
                        tmpInterior = Direction.U;
                    }
                }
                default -> {
                    if ((lastDirection == Direction.U && tmpInterior == Direction.L) || (lastDirection == Direction.D && tmpInterior == Direction.R)) {
                        tmpInterior = Direction.D;
                        points.get(points.size() - 1).interior.add(tmpInterior);
                    } else {
                        if (lastDirection == Direction.U && tmpInterior == Direction.R) {
                            if (points.size() > 0) points.get(points.size() - 1).interior.add(Direction.U);
                        }
                        tmpInterior = Direction.U;
                    }
                }
            }

            for (int i = 0; i < digInstruction.steps; i++) {
                Point point = new Point(lastX, lastY);

                switch (digInstruction.direction) {
                    case U -> {
                        point.y = point.y - (i + 1);
                    }
                    case D -> {
                        point.y = point.y + (i + 1);
                    }
                    case R -> {
                        point.x = point.x + (i + 1);
                    }
                    default -> {
                        point.x = point.x - (i + 1);
                    }
                }

                Field field = new Field(true, point);
                field.interior.add(tmpInterior);
                points.add(field);
            }
            lastDirection = digInstruction.direction;
            lastX = points.get(points.size() - 1).point.x;
            lastY = points.get(points.size() - 1).point.y;
        }
        points.get(points.size()-1).interior.add(points.get(0).interior.get(0));

    }

    Optional<Field> findNearestPointTop(Point point) {
        Point tmpPoint = new Point(point.x, point.y);
        for (int i = point.y - 1; i >= 0; i--) {
            tmpPoint.y = i;
            Optional<Field> topPoint = points.stream().filter(_point -> _point.point.x == tmpPoint.x && _point.point.y == tmpPoint.y).findFirst();
            if (topPoint.isPresent()) {
                return topPoint;
            }
        }
        return Optional.empty();
    }

    Optional<Field> findNearestPointBottom(Point point) {
        Point tmpPoint = new Point(point.x, point.y);
        for (int i = point.y + 1; i < maxY + 1; i++) {
            tmpPoint.y = i;
            Optional<Field> bottomPoint = points.stream().filter(_point -> _point.point.x == tmpPoint.x && _point.point.y == tmpPoint.y).findFirst();
            if (bottomPoint.isPresent()) {
                return bottomPoint;
            }
        }
        return Optional.empty();
    }

    Optional<Field> findNearestPointLeft(Point point) {
        Point tmpPoint = new Point(point.x, point.y);
        for (int i = point.x - 1; i >= 0; i--) {
            tmpPoint.x = i;
            Optional<Field> leftPoint = points.stream().filter(_point -> _point.point.x == tmpPoint.x && _point.point.y == tmpPoint.y).findFirst();
            if (leftPoint.isPresent()) {
                return leftPoint;
            }
        }
        return Optional.empty();
    }

    Optional<Field> findNearestPointRight(Point point) {
        Point tmpPoint = new Point(point.x, point.y);
        for (int i = point.x + 1; i < maxX + 1; i++) {
            tmpPoint.x = i;
            Optional<Field> rightPoint = points.stream().filter(_point -> _point.point.x == tmpPoint.x && _point.point.y == tmpPoint.y).findFirst();
            if (rightPoint.isPresent()) {
                return rightPoint;
            }
        }
        return Optional.empty();
    }

    boolean pointIsInternal(Point point) {
        Optional<Field> topField = findNearestPointTop(point);
        if (topField.isPresent()) {
            if (!topField.get().interior.contains(Direction.D)) {
                return false;
            }
        }
        Optional<Field> bottomField = findNearestPointBottom(point);
        if (bottomField.isPresent()) {
            if (!bottomField.get().interior.contains(Direction.U)) {
                return false;
            }
        }
        Optional<Field> leftField = findNearestPointLeft(point);
        if (leftField.isPresent()) {
            if (!leftField.get().interior.contains(Direction.R)) {
                return false;
            }
        }
        Optional<Field> rightField = findNearestPointRight(point);

        if (rightField.isPresent()) {
            if (!rightField.get().interior.contains(Direction.L)) {
                return false;
            }
        }
        return true;
    }

    public int resolvePart1() {
        createPointsList();
        maxY = points.stream().map(point -> point.point.y).mapToInt(Integer::valueOf).max().getAsInt();
        maxX = points.stream().map(point -> point.point.x).mapToInt(Integer::valueOf).max().getAsInt();
        minY = points.stream().map(point -> point.point.y).mapToInt(Integer::valueOf).min().getAsInt();
        minX = points.stream().map(point -> point.point.x).mapToInt(Integer::valueOf).min().getAsInt();

        int lavaAmount = 0;
        System.out.println(minX + " " + minY + " " + maxX + " " + maxY);
        for (int i = minY; i < maxY + 1; i++) {
            for (int j = minX; j < maxX + 1; j++) {
                Point mapPoint = new Point(j, i);
                if (points.stream().map(point -> point.point).toList().contains(mapPoint) || pointIsInternal(mapPoint)) {
                    lavaAmount++;
//                    System.out.print("#");
                } else {
//                    System.out.print(".");
                }
            }
//            System.out.println();
            System.out.println(i + " " + lavaAmount);
        }

        return lavaAmount;
    }
}

class DigInstruction {
    Direction direction;
    int steps;
    String color;

    public DigInstruction(Direction direction, int steps, String color) {
        this.direction = direction;
        this.steps = steps;
        this.color = color;
    }
}

class Field {
    boolean isLava;
    List<Direction> interior = new ArrayList<>();
    Point point;

    public Field(boolean isLava, Point point) {
        this.isLava = isLava;
        this.point = point;
    }
}
