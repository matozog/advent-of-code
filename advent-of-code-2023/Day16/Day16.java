package Day16;

import lombok.EqualsAndHashCode;
import utils.FileUtils;
import utils.Point;
import utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

public class Day16 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;

    List<Beam> beams = new ArrayList<>();
    char[][] map;

    public Day16() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2023/day16/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        String[] lines = fLines.split("\n");
        map = new char[lines.length][];
        for (int i = 0; i < map.length; i++) {
            map[i] = lines[i].toCharArray();
        }
    }

    Beam moveBeam(Beam beam) {
        Coordinate nextCoordinates = null;
        nextCoordinates = beam.moveBeamAndGetNewCoordinates(map);
        return nextCoordinates != null ? new Beam(nextCoordinates) : null;

    }

    void iterate(List<Beam> beams) {
        List<Beam> beamAfterSplit = new ArrayList<>();
        Beam tmpBeam;
        for (Beam beam : beams) {
            if (!beam.finished) {
                tmpBeam = moveBeam(beam);
                if (tmpBeam != null) {
                    beamAfterSplit.add(tmpBeam);
                }
            }
        }

        beams.addAll(beamAfterSplit);
    }

    int getEnergizedTilesNumber() {
        return Beam.visitedFields.stream().map(path -> new Point(path.x, path.y)).collect(Collectors.toSet()).size();
    }

    public int resolvePart1() {
        Beam beam = new Beam(new Coordinate(0, 0, Direction.RIGHT));
        Beam.visitedFields.add(beam.coordinate);
        beams.add(beam);

        while (beams.stream().anyMatch(_beam -> !_beam.finished)) {
            iterate(beams);
        }

        return getEnergizedTilesNumber();
    }

    public int resolvePart2() {
        List<Beam> beamsToVerify = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0) {
                    beamsToVerify.add(new Beam(new Coordinate(j, i, Direction.DOWN)));
                }
                if (i == map.length - 1) {
                    beamsToVerify.add(new Beam(new Coordinate(j, i, Direction.UP)));
                }
            }
            beamsToVerify.add(new Beam(new Coordinate(0, i, Direction.RIGHT)));
            beamsToVerify.add(new Beam(new Coordinate(map[i].length - 1, i, Direction.LEFT)));
        }
        int max = 0;
        for (Beam beam : beamsToVerify) {
            List<Beam> beamsList = new ArrayList<>();
            beamsList.add(beam);
            Beam.visitedFields.clear();
            Beam.visitedFields.add(beam.coordinate);
            if (beam.coordinate.x == 3 && beam.coordinate.y == 0) {
                System.out.println();
            }
            while (beamsList.stream().anyMatch(_beam -> !_beam.finished)) {
                iterate(beamsList);
            }
            int energizedTiles = getEnergizedTilesNumber();
            if (energizedTiles > max) {
                max = energizedTiles;
            }
        }
        return max;
    }
}

@EqualsAndHashCode
class Coordinate {
    int x, y;
    Direction direction;

    public Coordinate(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
}

class Beam {
    static Set<Coordinate> visitedFields = new HashSet<>();
    Coordinate coordinate;
    boolean finished = false;

    public Beam(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    Coordinate moveBeamAndGetNewCoordinates(char[][] map) {
        if (this.coordinate.x < 0 || this.coordinate.y < 0 || this.coordinate.x >= map[0].length || this.coordinate.y >= map.length) {
            return null;
        }

        Coordinate newCoordinate = new Coordinate(this.coordinate.x, this.coordinate.y, this.coordinate.direction);
        Coordinate splitterCoordinates = null;
        char fieldType = map[newCoordinate.y][newCoordinate.x];
        if (fieldType == '\\') {
            switch (newCoordinate.direction) {
                case UP -> {
                    newCoordinate.x = newCoordinate.x - 1;
                    newCoordinate.direction = Direction.LEFT;
                }
                case DOWN -> {
                    newCoordinate.x = newCoordinate.x + 1;
                    newCoordinate.direction = Direction.RIGHT;
                }
                case RIGHT -> {
                    newCoordinate.y = newCoordinate.y + 1;
                    newCoordinate.direction = Direction.DOWN;
                }
                default -> {
                    newCoordinate.y = newCoordinate.y - 1;
                    newCoordinate.direction = Direction.UP;
                }
            }
        } else if (fieldType == '|') {
            switch (newCoordinate.direction) {
                case UP -> newCoordinate.y = newCoordinate.y - 1;
                case DOWN -> newCoordinate.y = newCoordinate.y + 1;
                default -> {
                    splitterCoordinates = new Coordinate(newCoordinate.x, newCoordinate.y + 1, Direction.DOWN);
                    newCoordinate.y = newCoordinate.y - 1;
                    newCoordinate.direction = Direction.UP;
                }
            }
        } else if (fieldType == '-') {
            switch (newCoordinate.direction) {
                case LEFT -> newCoordinate.x = newCoordinate.x - 1;
                case RIGHT -> newCoordinate.x = newCoordinate.x + 1;
                default -> {
                    splitterCoordinates = new Coordinate(newCoordinate.x + 1, newCoordinate.y, Direction.RIGHT);
                    newCoordinate.x = newCoordinate.x - 1;
                    newCoordinate.direction = Direction.LEFT;
                }
            }
        } else if (fieldType == '/') {
            switch (newCoordinate.direction) {
                case UP -> {
                    newCoordinate.x = newCoordinate.x + 1;
                    newCoordinate.direction = Direction.RIGHT;
                }
                case DOWN -> {
                    newCoordinate.x = newCoordinate.x - 1;
                    newCoordinate.direction = Direction.LEFT;
                }
                case RIGHT -> {
                    newCoordinate.y = newCoordinate.y - 1;
                    newCoordinate.direction = Direction.UP;
                }
                default -> {
                    newCoordinate.y = newCoordinate.y + 1;
                    newCoordinate.direction = Direction.DOWN;
                }
            }
        } else {
            switch (newCoordinate.direction) {
                case UP -> {
                    newCoordinate.y = newCoordinate.y - 1;
                }
                case DOWN -> {
                    newCoordinate.y = newCoordinate.y + 1;
                }
                case RIGHT -> {
                    newCoordinate.x = newCoordinate.x + 1;
                }
                default -> {
                    newCoordinate.x = newCoordinate.x - 1;
                }
            }
        }

        if (!Utils.isInBounds(newCoordinate.x, newCoordinate.y, map.length, map[0].length) || visitedFields.contains(newCoordinate)) {
            this.finished = true;
        } else {
            this.coordinate = newCoordinate;
            visitedFields.add(newCoordinate);
        }

        if (splitterCoordinates != null && Utils.isInBounds(splitterCoordinates.x, splitterCoordinates.y, map[0].length, map.length)) {
            visitedFields.add(splitterCoordinates);
            return splitterCoordinates;
        }

        return null;
    }

}


