package Day14;

import utils.FileUtils;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day14 {
    FileUtils fileUtils = new FileUtils();
    String fileLines;
    char[][] map;

    public Day14() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("advent-of-code-2023/day14/input.txt", "\n");

        fillDataStructures(fileLines);
    }

    void fillDataStructures(String fLines) {
        String[] rows = fLines.split("\n");
        map = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            map[i] = rows[i].toCharArray();
        }
    }

    void moveMapElementsToNorth() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'O') {
                    for (int k = i; k > 0; k--) {
                        if (map[k - 1][j] == '.') {
                            map[k][j] = '.';
                            map[k - 1][j] = 'O';
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    void moveMapElementsToSouth() {
        for (int i = map.length - 1; i >= 0; i--) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 'O') {
                    for (int k = i; k < map.length - 1; k++) {
                        if (map[k + 1][j] == '.') {
                            map[k][j] = '.';
                            map[k + 1][j] = 'O';
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    void moveMapElementsToWest() {
        for (int i = 0; i < map[0].length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[j][i] == 'O') {
                    for (int k = i; k > 0; k--) {
                        if (map[j][k - 1] == '.') {
                            map[j][k] = '.';
                            map[j][k - 1] = 'O';
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    void moveMapElementsToEast() {
        for (int i = map.length - 1; i >= 0; i--) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[j][i] == 'O') {
                    for (int k = i; k < map.length - 1; k++) {
                        if (map[j][k + 1] == '.') {
                            map[j][k] = '.';
                            map[j][k + 1] = 'O';
                        } else {
                            break;
                        }
                    }
                }
            }
        }
    }

    long getTotalLoad() {
        long sum = 0L;
        for (int i = 0; i < map.length; i++) {
            sum += (long) CharBuffer.wrap(map[i])
                    .chars()
                    .mapToObj(character -> (char) character)
                    .filter(field -> field == 'O')
                    .toArray().length * (map.length - i);
        }
        return sum;
    }

    public long resolvePart1() {
        moveMapElementsToNorth();
        return getTotalLoad();
    }

    boolean areMapsSame(char[][] map1, char[][] map2) {
        for (int i = 0; i < map1.length; i++) {
            if (!Arrays.equals(map1[i], map2[i])) {
                return false;
            }
        }
        return true;
    }

    public long resolvePart2() {
        List<String> mapList = new ArrayList<>();
        List<String> tmpList = new ArrayList<>();

        long iterations = 1000000000L;
        int distance = 0, currentDistance = 1;
        int lastIndex = 0;
        String firstSequence = "";
        String tmpMap = Arrays.deepToString(map);
        mapList.add(tmpMap);

        while (mapList.size() != 1000) {
            moveMapElementsToNorth();
            moveMapElementsToWest();
            moveMapElementsToSouth();
            moveMapElementsToEast();
            tmpMap = Arrays.deepToString(map);
            if (mapList.contains(tmpMap)) {
                if (firstSequence.equals("")) {
                    firstSequence = tmpMap;
                }
            }
            mapList.add(tmpMap);
            if(tmpMap.equals(firstSequence)) System.out.println(mapList.size());
        }
//
//        mapList.add(tmpMap);

//        int index = mapList.indexOf(tmpMap);

        for (int i = 0; i < (iterations-143) % 34; i++) {
            moveMapElementsToNorth();
            moveMapElementsToWest();
            moveMapElementsToSouth();
            moveMapElementsToEast();
        }

        return getTotalLoad();
    }
}
