package commons;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
