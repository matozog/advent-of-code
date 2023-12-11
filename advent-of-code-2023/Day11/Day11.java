package Day11;

import lombok.AllArgsConstructor;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

public class Day11 {

    FileUtils fileUtils = new FileUtils();
    String fileLines;

    char[][] map;
    List<Tile> galaxiesList = new ArrayList<>();

    List<Integer> rowIndexesWithoutGalaxy = new ArrayList<>();
    List<Integer> columnIndexesWithoutGalaxy = new ArrayList<>();

    public Day11() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2023/day11/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        String[] mapData = fLines.split("\n");
        map = new char[mapData.length][];
        for (int i = 0; i < mapData.length; i++) {
            map[i] = mapData[i].toCharArray();
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '#') {
                    galaxiesList.add(new Tile(i, j, String.valueOf(map[i][j])));
                }
            }
            if (Arrays.stream(mapData[i].split("")).allMatch(tile -> tile.equals("."))) {
                rowIndexesWithoutGalaxy.add(i);
            }
        }

        checkColumnsWithoutGalaxies();
    }

    void checkColumnsWithoutGalaxies() {
        for (int i = 0; i < map[0].length; i++) {
            boolean withoutGalaxy = true;
            for (char[] chars : map) {
                if (chars[i] != '.') {
                    withoutGalaxy = false;
                    break;
                }
            }
            if (withoutGalaxy)
                columnIndexesWithoutGalaxy.add(i);
        }
    }

    List<Pair> getPairs() {
        List<Pair> pairs = new ArrayList<>();

        for (int i = 0; i < galaxiesList.size() - 1; i++) {
            for (int j = i + 1; j < galaxiesList.size(); j++) {
                pairs.add(new Pair(galaxiesList.get(i), galaxiesList.get(j)));
            }
        }

        return pairs;
    }

    public long resolvePart1() {
        List<Pair> pairs = getPairs();
        for (Tile tile : galaxiesList) {
            tile.updateLocation(rowIndexesWithoutGalaxy, columnIndexesWithoutGalaxy, 1);
        }
        return pairs.stream().map(pair -> pair.left.getDistanceToTile(pair.right)).mapToLong(Long::valueOf).sum();
    }

    public long resolvePart2() {
        List<Pair> pairs = getPairs();
        for (Tile tile : galaxiesList) {
            tile.updateLocation(rowIndexesWithoutGalaxy, columnIndexesWithoutGalaxy, 999999L);
        }
        return pairs.stream().map(pair -> pair.left.getDistanceToTile(pair.right)).mapToLong(Long::valueOf).sum();
    }
}

class Tile {
    long row, column;
    String type;

    public Tile(int row, int column, String type) {
        this.row = row;
        this.column = column;
        this.type = type;
    }

    public void updateLocation(List<Integer> rowIndexesWithoutGalaxy, List<Integer> columnIndexesWithoutGalaxy, long multiplier) {
        int numberOfLowerRows = rowIndexesWithoutGalaxy.stream().filter(index -> this.row > index).toList().size();
        int numberOfLowerColumns = columnIndexesWithoutGalaxy.stream().filter(index -> this.column > index).toList().size();

        this.row += numberOfLowerRows * multiplier;
        this.column += numberOfLowerColumns * multiplier;
    }

    public long getDistanceToTile(Tile tile) {
        return abs(this.row - tile.row) + abs(this.column - tile.column);
    }
}

@AllArgsConstructor
class Pair {
    Tile left, right;
}
