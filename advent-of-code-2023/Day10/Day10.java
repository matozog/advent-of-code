package Day10;

import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day10 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;

    Tile start;
    List<Tile[]> tilesMap = new ArrayList<>();

    public Day10() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2023/day10/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        String[] lines = fLines.split("\n");
        for (int i = 0; i < lines.length; i++) {
            char[] lineChars = lines[i].toCharArray();
            Tile[] tilesRow = new Tile[lineChars.length];
            for (int j = 0; j < lineChars.length; j++) {
                Tile tile = new Tile(String.valueOf(lineChars[j]), i, j);
                tilesRow[j] = tile;
                if (lineChars[j] == 'S') {
                    start = tile;
                }
            }
            tilesMap.add(tilesRow);
        }
    }

    int findDistanceToStart(Coordinate coordinate, Coordinate previousTile) {
        int steps = 1;
        Coordinate currentCoordinate = coordinate;
        Coordinate previousTileCoordinate = previousTile;
        while (currentCoordinate.row != start.coordinate.row || currentCoordinate.column != start.coordinate.column) {
            Coordinate nextNeighbour = tilesMap.get(currentCoordinate.row)[currentCoordinate.column].calculateNextTileIndexes(previousTileCoordinate.row, previousTileCoordinate.column);
            if (nextNeighbour.row == -1 && nextNeighbour.column == -1) {
                break;
            }
            steps++;
            previousTileCoordinate = currentCoordinate;
            currentCoordinate = nextNeighbour;
        }
        return steps;
    }

    public int resolvePart1() {
        List<Tile> startNeighbours = new ArrayList<>();

        if (start.coordinate.row - 1 >= 0) {
            startNeighbours.add(tilesMap.get(start.coordinate.row - 1)[start.coordinate.column]);
        }
        if (start.coordinate.row + 1 < tilesMap.size()) {
            startNeighbours.add(tilesMap.get(start.coordinate.row + 1)[start.coordinate.column]);
        }
        if (start.coordinate.column - 1 >= 0) {
            startNeighbours.add(tilesMap.get(start.coordinate.row)[start.coordinate.column - 1]);
        }
        if (start.coordinate.column + 1 < tilesMap.get(0).length) {
            startNeighbours.add(tilesMap.get(start.coordinate.row)[start.coordinate.column + 1]);
        }

        int[] distances = new int[startNeighbours.size()];
        int index = 0;
        for (Tile neighbour : startNeighbours) {
            distances[index] = findDistanceToStart(neighbour.coordinate, start.coordinate);
            index++;
        }

        return Arrays.stream(distances).max().getAsInt() / 2;
    }

    public int resolvePart2() {
        return 0;
    }
}

class Coordinate {
    int row, column;

    public Coordinate() {
    }

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }
}

class Tile {
    String type;
    Coordinate coordinate;

    public Tile(String type, int row, int column) {
        this.type = type;
        this.coordinate = new Coordinate(row, column);
    }

    // |
    Coordinate getNextTileIndexesForVerticalPipe(int inputRow, int inputColumn) {
        Coordinate nextTile = new Coordinate();
        if (this.coordinate.row < inputRow) {
            nextTile.row = this.coordinate.row - 1;
            nextTile.column = this.coordinate.column;
        } else if (this.coordinate.row > inputRow) {
            nextTile.row = this.coordinate.row + 1;
            nextTile.column = this.coordinate.column;
        } else {
            nextTile.row = -1;
            nextTile.column = -1;
        }
        return nextTile;
    }

    // -
    Coordinate getNextTileIndexesForHorizontalPipe(int inputRow, int inputColumn) {
        Coordinate nextTile = new Coordinate();
        if (this.coordinate.column > inputColumn) {
            nextTile.row = this.coordinate.row;
            nextTile.column = this.coordinate.column + 1;
        } else if (this.coordinate.column < inputColumn) {
            nextTile.row = this.coordinate.row;
            nextTile.column = this.coordinate.column - 1;
        } else {
            nextTile.row = -1;
            nextTile.column = -1;
        }
        return nextTile;
    }

    // L
    Coordinate getNextTileIndexesFor90DegreeBendNorthAndEast(int inputRow, int inputColumn) {
        Coordinate nextTile = new Coordinate();
        if (this.coordinate.row > inputRow) {
            nextTile.row = this.coordinate.row;
            nextTile.column = this.coordinate.column + 1;
        } else if (this.coordinate.row == inputRow && this.coordinate.column < inputColumn) {
            nextTile.row = this.coordinate.row - 1;
            nextTile.column = this.coordinate.column;
        } else {
            nextTile.row = -1;
            nextTile.column = -1;
        }
        return nextTile;
    }

    // J
    Coordinate getNextTileIndexesFor90DegreeBendNorthAndWest(int inputRow, int inputColumn) {
        Coordinate nextTile = new Coordinate();
        if (this.coordinate.row > inputRow) {
            nextTile.row = this.coordinate.row;
            nextTile.column = this.coordinate.column - 1;
        } else if (this.coordinate.row == inputRow && this.coordinate.column > inputColumn) {
            nextTile.row = this.coordinate.row - 1;
            nextTile.column = this.coordinate.column;
        } else {
            nextTile.row = -1;
            nextTile.column = -1;
        }
        return nextTile;
    }

    // 7
    Coordinate getNextTileIndexesFor90DegreeBendSouthAndWest(int inputRow, int inputColumn) {
        Coordinate nextTile = new Coordinate();
        if (this.coordinate.row < inputRow) {
            nextTile.row = this.coordinate.row;
            nextTile.column = this.coordinate.column - 1;
        } else if (this.coordinate.row == inputRow && this.coordinate.column > inputColumn) {
            nextTile.row = this.coordinate.row + 1;
            nextTile.column = this.coordinate.column;
        } else {
            nextTile.row = -1;
            nextTile.column = -1;
        }
        return nextTile;
    }

    // F
    Coordinate getNextTileIndexesFor90DegreeBendSouthAndEast(int inputRow, int inputColumn) {
        Coordinate nextTile = new Coordinate();
        if (this.coordinate.row < inputRow) {
            nextTile.row = this.coordinate.row;
            nextTile.column = this.coordinate.column + 1;
        } else if (this.coordinate.row == inputRow && this.coordinate.column < inputColumn) {
            nextTile.row = this.coordinate.row + 1;
            nextTile.column = this.coordinate.column;
        }
        return nextTile;
    }

    // .
    Coordinate getNextTileIndexesForGround() {
        return new Coordinate(-1, -1);
    }

    public Coordinate calculateNextTileIndexes(int inputRow, int inputColumn) {
        return switch (this.type) {
            case "|" -> getNextTileIndexesForVerticalPipe(inputRow, inputColumn);
            case "-" -> getNextTileIndexesForHorizontalPipe(inputRow, inputColumn);
            case "L" -> getNextTileIndexesFor90DegreeBendNorthAndEast(inputRow, inputColumn);
            case "J" -> getNextTileIndexesFor90DegreeBendNorthAndWest(inputRow, inputColumn);
            case "7" -> getNextTileIndexesFor90DegreeBendSouthAndWest(inputRow, inputColumn);
            case "F" -> getNextTileIndexesFor90DegreeBendSouthAndEast(inputRow, inputColumn);
            case "S" -> this.coordinate;
            default -> getNextTileIndexesForGround();
        };

    }

}
