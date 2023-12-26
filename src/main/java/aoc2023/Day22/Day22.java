package aoc2023.Day22;

import aoc2023.utils.Point3D;
import aoc2023.utils.Utils;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day22 {
    utils.FileUtils fileUtils = new utils.FileUtils();
    String fileLines;

    List<Brick> brickList = new ArrayList<>();
    int maxHeight;

    public Day22() {
        fileLines = fileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2023/day22/input.txt", "\n");

        fillDataStructures();
    }

    void fillDataStructures() {
        String[] bricksLine = fileLines.split("\n");

        Arrays.stream(bricksLine).forEach(brickLine -> {
            String[] brickEnds = brickLine.split("~");
            String[] brickStart = brickEnds[0].split(",");
            String[] brickEnd = brickEnds[1].split(",");
            Point3D startPoint = new Point3D(Integer.parseInt(brickStart[0]), Integer.parseInt(brickStart[1]), Integer.parseInt(brickStart[2]));
            Point3D endPoint = new Point3D(Integer.parseInt(brickEnd[0]), Integer.parseInt(brickEnd[1]), Integer.parseInt(brickEnd[2]));

            brickList.add(new Brick(startPoint, endPoint));
        });
    }

    void fallBricksOnLine(int lineIndex) {
        List<Brick> bricksToFall = brickList.stream().filter(brick -> brick.startHeight == lineIndex).toList();

        for (Brick brick : bricksToFall) {
            boolean isEnableToFall = true;
            AtomicInteger i = new AtomicInteger(lineIndex);
            while (isEnableToFall && i.get() != 1) {
                i.set(i.get() - 1);
                isEnableToFall = brick.isEnableToFall(brickList.stream().filter(_brick -> _brick.endHeight == i.get()).toList());
            }
            i.set(i.get() + 1);
            int zDifference = brick.end.z - brick.start.z;
            brick.start.z = i.get();
            brick.end.z = i.get() + zDifference;
            brick.startHeight = brick.start.z;
            brick.endHeight = brick.end.z;
        }
    }

    void fallBricks() {
        for (int i = 2; i <= maxHeight; i++) {
            fallBricksOnLine(i);
        }
    }

    public int resolvePart1() {
        maxHeight = brickList.stream().map(brick -> brick.startHeight).mapToInt(Integer::valueOf).max().getAsInt();

        fallBricks();

        int brickToDisIntegrate = 0;
        for (Brick brick : brickList) {
            List<Brick> bricksToCheck = brickList.stream().filter(_brick -> _brick.startHeight == brick.endHeight + 1).toList();
            if (bricksToCheck.stream().noneMatch(_brick -> _brick.isEnableToFall(brickList.stream().filter(b -> b.startHeight == brick.endHeight && !b.equals(brick)).toList()))) {
                brickToDisIntegrate++;
            }
        }
        return brickToDisIntegrate;
    }
}

@EqualsAndHashCode
class Brick {
    Point3D start, end;
    int startHeight;
    int endHeight;

    public Brick(Point3D start, Point3D end) {
        this.start = start;
        this.end = end;
        this.startHeight = Math.min(start.z, end.z);
        this.endHeight = Math.max(start.z, end.z);
    }

    public boolean isEnableToFall(List<Brick> bricks) {
        for (Brick brick : bricks) {
            if (Utils.isLineIntersection(this.start, this.end, brick.start, brick.end)) {
                return false;
            }
        }
        return true;
    }
}
