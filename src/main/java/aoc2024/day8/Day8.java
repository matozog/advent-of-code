package aoc2024.day8;

import commons.FileUtils;
import commons.Point;
import commons.Task;
import commons.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day8 implements Task<Integer> {
    FileUtils fileUtils = new FileUtils();
    String fileLine;
    Set<Field> fields = new HashSet<>();
    int maxX = 0, maxY = 0;

    public Day8() {
        fileLine = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day8/input.txt", "\n");

        String[] lines = fileLine.split("\n");
        maxY = lines.length;

        IntStream.range(0, lines.length).forEach(index -> {
            char[] charArray = lines[index].toCharArray();
            if (maxX == 0) maxX = charArray.length;
            for (int i = 0; i < charArray.length; i++) {
                fields.add(new Field(new Point(i, index), charArray[i]));
            }
        });
    }

    Field createAntinode(Field field1, Field field2) {
        int xMove = field2.x - field1.x;
        int yMove = field2.y - field1.y;
        return new Field(new Point(field2.x + xMove, field2.y + yMove), '#');
    }

    ArrayList<Field> createAntinodes(Field field1, Field field2) {
        ArrayList<Field> antinodes = new ArrayList<>();
        int xMove = field2.x - field1.x;
        int yMove = field2.y - field1.y;
        Field antinode = new Field(new Point(field2.x + xMove, field2.y + yMove), '#');
        antinodes.add(antinode);
        antinodes.add(new Field(new Point(field2.x, field2.y), '#'));
        int index = 2;
        while (Utils.isInBounds(antinode.x, antinode.y, maxX, maxY)) {
            antinode = new Field(new Point(field2.x + xMove * index, field2.y + yMove * index), '#');
            if (Utils.isInBounds(antinode.x, antinode.y, maxX, maxY))
                antinodes.add(antinode);
            index++;
        }

        return antinodes;
    }

    @Override
    public Integer resolvePart1() {
        Set<Field> fieldsWithAntennas = fields.stream().filter(field -> field.type != '.').collect(Collectors.toSet());
        Set<Point> antinodePoints = new HashSet<>();

        fieldsWithAntennas.forEach(baseField -> {
            fieldsWithAntennas.stream().filter(f -> baseField.type == f.type && baseField != f).forEach((field2 -> {
                Field antinode = createAntinode(baseField, field2);
                if (fields.contains(antinode))
                    antinodePoints.add(antinode);
            }));
        });

        return antinodePoints.size();
    }

    @Override
    public Integer resolvePart2() {
        Set<Field> fieldsWithAntennas = fields.stream().filter(field -> field.type != '.').collect(Collectors.toSet());
        Set<Point> antinodePoints = new HashSet<>();

        fieldsWithAntennas.forEach(baseField -> {
            fieldsWithAntennas.stream().filter(f -> baseField.type == f.type && baseField != f).forEach((field2 -> {
                ArrayList<Field> antinodes = createAntinodes(baseField, field2);
                antinodes.forEach(node -> {
                    if (fields.contains(node))
                        antinodePoints.add(node);
                });
            }));
        });

        return antinodePoints.size();
    }
}

class Field extends Point {
    char type;

    public Field(Point point, char type) {
        super(point.x, point.y);
        this.type = type;
    }
}
