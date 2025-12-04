package aoc2025.day3

import commons.FileUtils
import kotlin.math.pow

fun main() {
    val fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/kotlin/aoc2025/day3/input.txt", "\n");

    val rows = fileLines.split("\n");

    println("Part 1: " + part1(rows))
    println("Part 2: " + part2(rows))
}

fun findMaxFromSubstring(values: String, prevPosition: Int, amountOfDigits: Int, currentDigit: Int): Int {
    var position = prevPosition;

    for (i in (position..values.length - amountOfDigits + currentDigit)) {
        val num = values[i].toString().toLong()

        if (num > values[position].toString().toLong()) {
            position = i
        }
    }
    return position;
}

fun part1(rows: List<String>): Int {
    var sum = 0;
    val amountOfDigits = 2

    for (row in rows) {
        var prevPosition = -1;
        for (i in (0..<amountOfDigits)) {
            val highestPosition = findMaxFromSubstring(row, prevPosition + 1, amountOfDigits, i);
            prevPosition = highestPosition
            sum += row[highestPosition].toString().toInt() * 10.0.pow(amountOfDigits - 1 - i).toInt();
        }
    }

    return sum;
}

fun part2(rows: List<String>): Long {
    var sum = 0L
    val amountOfDigits = 12;

    for (row in rows) {
        var prevPosition = -1;
        for (i in (0..<amountOfDigits)) {
            val highestPosition = findMaxFromSubstring(row, prevPosition + 1, amountOfDigits, i);
            prevPosition = highestPosition
            sum += row[highestPosition].toString().toLong() * 10.0.pow(amountOfDigits - 1 - i).toLong();
        }
    }

    return sum
}