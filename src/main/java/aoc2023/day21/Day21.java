package aoc2023.day21;

import commons.FileUtils;
import commons.Point;
import commons.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day21 implements Task<Long> {
    FileUtils fileUtils = new FileUtils();
    String fileLines;

    char[][] map;
    List<Point> tilesToReach = new ArrayList<>();
    List<Point> pointsToRemove = new ArrayList<>();
    Set<Point> reachedTiles = new HashSet<>();

    public Day21() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day21/input.txt", "\n");

        fillDataStructures();
    }

    void fillDataStructures() {
        String[] linesData = fileLines.split("\n");
        map = new char[linesData.length][];
        for (int i = 0; i < linesData.length; i++) {
            map[i] = linesData[i].toCharArray();
        }
    }

    void calculateTilesToReach(int x, int y) {
        //NORTH
        if (y > 0 && map[y - 1][x] != '#') {
            tilesToReach.add(new Point(x, y - 1));
            pointsToRemove.add(new Point(x, y));
        }
        // SOUTH
        if (y < map.length - 1 && map[y + 1][x] != '#') {
            tilesToReach.add(new Point(x, y + 1));
            pointsToRemove.add(new Point(x, y));
        }
        // WEST
        if (x > 0 && map[y][x - 1] != '#') {
            tilesToReach.add(new Point(x - 1, y));
            pointsToRemove.add(new Point(x, y));
        }
        // EAST
        if (x < map[0].length - 1 && map[y][x + 1] != '#') {
            tilesToReach.add(new Point(x + 1, y));
            pointsToRemove.add(new Point(x, y));
        }
    }

    void makeStep() {
        for (Point point : reachedTiles) {
            calculateTilesToReach(point.x, point.y);
        }

        for (Point point : pointsToRemove) {
            reachedTiles.remove(point);
        }
        reachedTiles.addAll(tilesToReach);
        tilesToReach.clear();
    }

    Point findSPoint() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'S') {
                    map[i][j] = 'O';
                    return new Point(j, i);
                }
            }
        }
        return null;
    }

    public Long resolvePart1() {
        int STEPS_AMOUNT = 64;
        reachedTiles.add(findSPoint());
        for (int i = 0; i < STEPS_AMOUNT; i++) {
            makeStep();
        }

        return (long) reachedTiles.size();
    }

    public Long resolvePart2() {
        return 0L;
    }
}
