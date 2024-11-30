package aoc2022.day22;

import commons.Task;
import commons.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;

enum MapFieldValue {
    RIGHT,
    BOTTOM,
    LEFT,
    UP,
    NOT_IN_MAP,
    FIELD,
    WALL,
}

public class Day22 implements Task {
    FileUtils fileUtils;
    private ArrayList<Field[]> map = new ArrayList<>();
    int numbersOfColumn, numberOfRows;
    ArrayList<String> moves;
    Field currentField;

    public Day22() {
        fileUtils = new FileUtils();

        String fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2022/day22/input.txt", "\n");

        String[] linesArr = fileLines.split("\n");
        fillArraysWithInputData(linesArr);
    }

    MapFieldValue parseCharToEnum(char mapChar) {
        return switch (mapChar) {
            case '.' -> MapFieldValue.FIELD;
            case '#' -> MapFieldValue.WALL;
            default -> MapFieldValue.NOT_IN_MAP;
        };
    }

    char parseEnumToChar(MapFieldValue fieldValue) {
        return switch (fieldValue) {
            case FIELD -> '.';
            case WALL -> '#';
            case RIGHT -> '>';
            case LEFT -> '<';
            case UP -> '^';
            case BOTTOM -> 'v';
            default -> ' ';
        };
    }

    Field[] parseLine(String line, int rowIndex) {
        char[] charsInLine = line.toCharArray();
        Field[] field = new Field[numbersOfColumn];
        for (int i = 0; i < numbersOfColumn; i++) {
            field[i] = new Field();
            field[i].x = i;
            field[i].y = rowIndex;
            if (i < charsInLine.length)
                field[i].fieldValue = parseCharToEnum(charsInLine[i]);
            else
                field[i].fieldValue = MapFieldValue.NOT_IN_MAP;
        }
        return field;
    }

    int findMapWidth(String[] fileLines) {
        int mapWidth = 0;
        for (String fileLine : fileLines) {
            if (fileLine.length() > mapWidth) mapWidth = fileLine.length();
            if (fileLine.isEmpty()) break;
        }
        return mapWidth;
    }

    ArrayList<String> parseMovesLine(String line) {
        char[] movesChars = line.toCharArray();
        ArrayList<String> tmpMoves = new ArrayList<>();
        StringBuilder moveNumberBuilder = new StringBuilder();
        for (int i = 0; i < movesChars.length; i++) {
            if (movesChars[i] == 'R' || movesChars[i] == 'L') {
                if (!moveNumberBuilder.isEmpty()) {
                    tmpMoves.add(moveNumberBuilder.toString());
                    moveNumberBuilder.setLength(0);
                }
                tmpMoves.add(String.valueOf(movesChars[i]));
            } else {
                moveNumberBuilder.append(movesChars[i]);
            }
        }
        tmpMoves.add(moveNumberBuilder.toString());

        return tmpMoves;
    }

    void displayMap() {
        map.forEach(row -> {
            Arrays.stream(row).forEach(el -> System.out.print(parseEnumToChar(el.fieldValue)));
            System.out.println();
        });
    }

    private void fillArraysWithInputData(String[] fileLines) {
        numbersOfColumn = findMapWidth(fileLines);
        boolean emptyLine = false;
        for (int i = 0; i < fileLines.length; i++) {
            if (emptyLine) {
                moves = parseMovesLine(fileLines[i]);
            } else {
                map.add(parseLine(fileLines[i], i));
            }
            if (fileLines[i].isEmpty()) {
                emptyLine = true;
            }
        }
        numberOfRows = map.size();
        displayMap();
        System.out.println(moves);
    }

    Field findFirstEmptyFieldInList(Field[] fieldsList) {
        return Arrays.stream(fieldsList).filter(el -> el.fieldValue == MapFieldValue.FIELD).findFirst().orElseThrow();
    }

    Field findStartElement() {
        Field startElement = null;
        for (int i = 0; i < map.size(); i++) {
            startElement = findFirstEmptyFieldInList(map.get(i));
            if (startElement != null) break;
        }

        return startElement;
    }

    Field findNextWrappedField(MapFieldValue mapFieldValue) {
        Field nextWrappedField = null;
        switch (mapFieldValue) {
            case RIGHT:
                nextWrappedField = Arrays.stream(map.get(currentField.y))
                        .filter(field -> field.fieldValue != MapFieldValue.NOT_IN_MAP)
                        .findFirst()
                        .orElseThrow();
                break;
            case LEFT:
                for (int i = numbersOfColumn - 1; i >= 0; i--) {
                    if (map.get(currentField.y)[i].fieldValue != MapFieldValue.NOT_IN_MAP) {
                        nextWrappedField = map.get(currentField.y)[i];
                        break;
                    }
                }
                break;
            case BOTTOM:
                for (int i = 0; i < numberOfRows; i++) {
                    if (map.get(i)[currentField.x].fieldValue != MapFieldValue.NOT_IN_MAP) {
                        nextWrappedField = map.get(i)[currentField.x];
                        break;
                    }
                }
                break;
            default:
                for (int i = numberOfRows - 1; i >= 0; i--) {
                    if (map.get(i)[currentField.x].fieldValue != MapFieldValue.NOT_IN_MAP) {
                        nextWrappedField = map.get(i)[currentField.x];
                        break;
                    }
                }
                break;

        }

        return nextWrappedField;

    }

    Field getNextField(int x, int y, MapFieldValue mapFieldValue) {
        if (x >= numbersOfColumn || x < 0 || y >= numberOfRows || y < 0) {
            return findNextWrappedField(mapFieldValue);
        }
        Field nextField = map.get(y)[x];
        if (nextField.fieldValue == MapFieldValue.NOT_IN_MAP) {
            nextField = findNextWrappedField(mapFieldValue);
        }

        return nextField;
    }

    void assignField(Field nextField, MapFieldValue mapFieldValue) {
        if (nextField.fieldValue == MapFieldValue.FIELD) {
            nextField.fieldValue = mapFieldValue;
            currentField.fieldValue = MapFieldValue.FIELD;
            currentField = nextField;
        }
        ;
    }

    void move(String move) {
        int numberOfStep = Integer.parseInt(move);
        Field nextField;

        if (currentField.fieldValue == MapFieldValue.RIGHT) {
            for (int i = 0; i < numberOfStep; i++) {
                nextField = getNextField(currentField.x + 1, currentField.y, MapFieldValue.RIGHT);
                assignField(nextField, MapFieldValue.RIGHT);
                if (nextField.fieldValue == MapFieldValue.WALL) break;
            }
        } else if (currentField.fieldValue == MapFieldValue.BOTTOM) {
            for (int i = 0; i < numberOfStep; i++) {
                nextField = getNextField(currentField.x, currentField.y + 1, MapFieldValue.BOTTOM);
                assignField(nextField, MapFieldValue.BOTTOM);
                if (nextField.fieldValue == MapFieldValue.WALL) break;
            }
        } else if (currentField.fieldValue == MapFieldValue.LEFT) {
            for (int i = 0; i < numberOfStep; i++) {
                nextField = getNextField(currentField.x - 1, currentField.y, MapFieldValue.LEFT);
                assignField(nextField, MapFieldValue.LEFT);
                if (nextField.fieldValue == MapFieldValue.WALL) break;
            }
        } else {
            for (int i = 0; i < numberOfStep; i++) {
                nextField = getNextField(currentField.x, currentField.y - 1, MapFieldValue.UP);
                assignField(nextField, MapFieldValue.UP);
                if (nextField.fieldValue == MapFieldValue.WALL) break;
            }
        }
    }

    void changeDirection(String move) {
        if (move.equals("R")) {
            if (currentField.fieldValue == MapFieldValue.RIGHT) {
                currentField.fieldValue = MapFieldValue.BOTTOM;
            } else if (currentField.fieldValue == MapFieldValue.BOTTOM) {
                currentField.fieldValue = MapFieldValue.LEFT;
            } else if (currentField.fieldValue == MapFieldValue.LEFT) {
                currentField.fieldValue = MapFieldValue.UP;
            } else if (currentField.fieldValue == MapFieldValue.UP) {
                currentField.fieldValue = MapFieldValue.RIGHT;
            }
        } else if (move.equals("L")) {
            if (currentField.fieldValue == MapFieldValue.RIGHT) {
                currentField.fieldValue = MapFieldValue.UP;
            } else if (currentField.fieldValue == MapFieldValue.BOTTOM) {
                currentField.fieldValue = MapFieldValue.RIGHT;
            } else if (currentField.fieldValue == MapFieldValue.LEFT) {
                currentField.fieldValue = MapFieldValue.BOTTOM;
            } else if (currentField.fieldValue == MapFieldValue.UP) {
                currentField.fieldValue = MapFieldValue.LEFT;
            }
        }
    }

    boolean isDirection(String move) {
        return move.equals("R") || move.equals("L");
    }

    @Override
    public Integer resolvePart1() {
        this.currentField = findStartElement();
        this.currentField.fieldValue = MapFieldValue.RIGHT;
        for (String move : moves) {
            if (isDirection(move)) {
                changeDirection(move);
            } else {
                move(move);
            }
        }
        displayMap();
        System.out.println(currentField);
        return 1000 * (this.currentField.y + 1) + 4 * (this.currentField.x + 1);
    }

    @Override
    public Integer resolvePart2() {
        return 0;
    }
}

class Field {
    int x, y;
    MapFieldValue fieldValue;

    @Override
    public String toString() {
        return "Field{" +
                "x=" + x +
                ", y=" + y +
                ", fieldValue=" + fieldValue +
                '}';
    }
}