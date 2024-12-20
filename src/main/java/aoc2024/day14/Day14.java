package aoc2024.day14;

import commons.FileUtils;
import commons.Point;
import commons.Task;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day14 implements Task<Integer> {
    String fileLine;
    List<Robot> robots = new ArrayList<>();
    int columns = 101, rows = 103, seconds = 100;

    public Day14() {
        fileLine = FileUtils.getFileLinesAsStringByDelimiter("src/main/java/aoc2024/day14/input.txt", "\n");

        Arrays.stream(fileLine.split("\n")).forEach(line -> {
            Pattern pattern = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String xCoord = matcher.group(1);
                String yCoord = matcher.group(2);
                String xVelocity = matcher.group(3);
                String yVelocity = matcher.group(4);
                robots.add(new Robot(new Point(Integer.parseInt(xCoord), Integer.parseInt(yCoord)), Integer.parseInt(xVelocity), Integer.parseInt(yVelocity)));
            }
        });

    }

    void move(Robot robot, int x, int y) {
        int newX = robot.position.x + x;
        int newY = robot.position.y + y;

        if (newX >= columns) {
            newX -= columns;
        } else if (newX < 0) {
            newX = columns + newX;
        }

        if (newY >= rows) {
            newY -= rows;
        } else if (newY < 0) {
            newY = rows + newY;
        }

        robot.position.x = newX;
        robot.position.y = newY;
    }

    @Override
    public Integer resolvePart1() {
        int firstQuarter = 0, secondQuarter = 0, thirdQuarter = 0, fourthQuarter = 0;

        for (Robot robot : robots) {
            move(robot, (robot.velX * seconds) % columns, (robot.velY * seconds) % rows);
            if (robot.position.x < columns / 2 && robot.position.y < rows / 2) firstQuarter++;
            else if (robot.position.x < columns / 2 && robot.position.y > rows / 2) thirdQuarter++;
            else if (robot.position.x > columns / 2 && robot.position.y < rows / 2) secondQuarter++;
            else if (robot.position.x > columns / 2 && robot.position.y > rows / 2) fourthQuarter++;
        }

        return firstQuarter * secondQuarter * thirdQuarter * fourthQuarter;
    }

    boolean areRobotsOverlay() {
        return robots.stream().allMatch(robot -> Collections.frequency(robots, robot) <= 1);
    }

    @Override
    public Integer resolvePart2() {
        int secondsToEasterEgg = 0;

        while (!areRobotsOverlay()) {
            secondsToEasterEgg++;
            for (Robot robot : robots) {
                move(robot, robot.velX, robot.velY);
            }
        }

        return secondsToEasterEgg;
    }
}

@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class Robot {
    @EqualsAndHashCode.Include
    Point position;
    @EqualsAndHashCode.Exclude
    int velX, velY;
}
