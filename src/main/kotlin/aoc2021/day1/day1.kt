package aoc2021.day1

import commons.FileUtils

fun main() {
    val fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/kotlin/aoc2021/day1/input.txt", "\n");

    val depths = fileLines.split("\n");

    println("Part 1: " + part1(depths))
}

fun part1(depths: List<String>): Int {
    var increased = 0;

    for (i in 1..<depths.size) {
        if (depths[i - 1].toInt() < depths[i].toInt()) {
            increased += 1;
        }
    }

    return increased;
}
