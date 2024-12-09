package aoc2024.day6;

import commons.FileUtils;
import commons.Point;
import commons.Task;
import commons.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Day6 implements Task<Integer> {
    String fileLine;
    Map<String, Field> fields = new HashMap<>();
    int maxX = 0, maxY;
    Guard guard;
    Map<Direction, Direction> mappedDirections = new HashMap<>() {{
        put(Direction.TOP, Direction.RIGHT);
        put(Direction.RIGHT, Direction.DOWN);
        put(Direction.DOWN, Direction.LEFT);
        put(Direction.LEFT, Direction.TOP);
    }};

    public Day6() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day6/input.txt", "\n");
        String[] lines = fileLine.split("\n");
        maxY = lines.length;
        IntStream.range(0, lines.length).forEach(index -> {
            char[] lineArray = lines[index].toCharArray();
            if (maxX == 0) maxX = lineArray.length;
            for (int i = 0; i < lineArray.length; i++) {
                String key = generateHashFromCoords(i, index);
                if (lineArray[i] == '^') {
                    guard = new Guard(i, index, Direction.TOP);
                    Field guardField = new Field(i, index, '.');
                    guardField.guardDirections.add(Direction.TOP);
                    fields.put(key, guardField);
                } else {
                    fields.put(key, new Field(i, index, lineArray[i]));
                }
            }
        });
    }

    String generateHashFromCoords(int x, int y) {
        return x + "_" + y;
    }

    boolean moveGuardAndDetectLoop(int x, int y) {
        Field nextField = fields.get(generateHashFromCoords(x, y));
        if (nextField == null || nextField.type != '#') {
            guard.x = x;
            guard.y = y;
            if (nextField != null) {
                nextField.visited = true;
                if (nextField.guardDirections.contains(guard.direction))
                    return true;
                nextField.guardDirections.add(guard.direction);
            }
        } else {
            guard.direction = mappedDirections.get(guard.direction);
        }

        return false;
    }

    boolean move() {
        return switch (guard.direction) {
            case TOP -> moveGuardAndDetectLoop(guard.x, guard.y - 1);
            case DOWN -> moveGuardAndDetectLoop(guard.x, guard.y + 1);
            case LEFT -> moveGuardAndDetectLoop(guard.x - 1, guard.y);
            default -> moveGuardAndDetectLoop(guard.x + 1, guard.y);
        };
    }

    @Override
    public Integer resolvePart1() {
        while (Utils.isInBounds(guard.x, guard.y, maxX, maxY)) {
            move();
        }
        return (int) fields.keySet().stream().filter(key -> fields.get(key).visited).count();
    }

    boolean isLoopInSimulation() {
        boolean loop = false;
        while (Utils.isInBounds(guard.x, guard.y, maxX, maxY) && !loop) {
            loop = move();
        }

        return loop;
    }

    void resetGuardDirectionsLists() {
        fields.keySet().forEach(key -> fields.get(key).guardDirections.clear());
    }

    @Override
    public Integer resolvePart2() {
        while (Utils.isInBounds(guard.x, guard.y, maxX, maxY)) {
            move();
        }
        guard.restartPosition();
        List<Field> visitedFields = fields.keySet()
                .stream()
                .filter(field -> fields.get(field).visited)
                .map(fields::get)
                .toList();
        resetGuardDirectionsLists();

        return visitedFields.stream()
                .map(field -> {
                    Field currentField = fields.get(generateHashFromCoords(field.x, field.y));
                    char currentType = currentField.type;
                    currentField.type = '#';
                    boolean isLoop = isLoopInSimulation();

                    // Reset positions and fields
                    resetGuardDirectionsLists();
                    guard.restartPosition();
                    currentField.type = currentType;
                    return isLoop;
                })
                .filter(loop -> loop).toList().size();
    }
}

class Field extends Point {
    boolean visited = false;
    List<Direction> guardDirections = new ArrayList<>();
    char type;

    public Field(int x, int y, char type) {
        super(x, y);
        this.type = type;
    }
}

enum Direction {
    TOP,
    DOWN,
    RIGHT,
    LEFT
}

class Guard extends Point {
    Direction direction;
    int startX, startY;
    Direction startDirection;

    public Guard(int x, int y, Direction direction) {
        super(x, y);
        startX = x;
        startY = y;
        this.direction = direction;
        this.startDirection = direction;
    }

    void restartPosition() {
        this.x = startX;
        this.y = startY;
        direction = startDirection;
    }
}
