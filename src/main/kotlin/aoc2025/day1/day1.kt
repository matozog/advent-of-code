package aoc2025.day1

import commons.FileUtils

fun main() {
    val fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/kotlin/aoc2025/day1/input.txt", "\n");

    val movements = fileLines.split("\n");

    println("Part 1: " + part1(movements))
    println("Part 2: " + part2(movements))
}

fun part1(movements: List<String>): Int {
    var pointToZero = 0;
    var position = 50;
    var steps: Int;

    for (move in movements) {
        steps = move.substring(1).toInt() % 100;

        position = if (move.first() == 'R') position + steps else position - steps;
        if (position > 99) {
            position -= 100;
        } else if (position < 0) {
            position += 100;
        }

        if (position == 0) {
            pointToZero++;
        }
    }

    return pointToZero;
}

fun part2(movements: List<String>): Int {
    var pointToZero = 0;
    var position = 50;
    var steps: Int;
    var additionalClicks: Int;
    var wasZero = false;

    for (move in movements) {
        steps = move.substring(1).toInt() % 100;
        additionalClicks = move.substring(1).toInt() / 100;

        position = if (move.first() == 'R') position + steps else position - steps;
        if (position > 99) {
            position -= 100;
            if (position != 0) {
                additionalClicks++;
            }
        } else if (position < 0) {
            position += 100;
            if (position != 0 && !wasZero) {
                additionalClicks++;
            }
        }

        wasZero = false;

        if (position == 0) {
            wasZero = true;
            pointToZero++;
        }

        pointToZero += additionalClicks;
    }

    return pointToZero;
}
