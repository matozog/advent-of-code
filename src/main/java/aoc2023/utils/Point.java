package aoc2023.utils;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
