package aoc2025.day2

import commons.FileUtils

fun main() {
    val fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/kotlin/aoc2025/day2/input.txt", "\n");

    val ranges = fileLines.split(",");

    println("Part 1: " + part1(ranges))
    println("Part 2: " + part2(ranges))
}

fun part1(ranges: List<String>): Long {
    var sum = 0L

    for (range in ranges) {
        val numbers = range.split("-");

        for (i in numbers[0].toLong()..numbers[1].toLong()) {
            val numberAsString = i.toString();
            if (numberAsString.length % 2 == 0) {
                if (numberAsString.substring(0, numberAsString.length / 2)
                        .toLong() == numberAsString.substring((numberAsString.length / 2), numberAsString.length)
                        .toLong()
                ) {
                    sum += i
                }
            }
        }
    }

    return sum;
}

fun part2(ranges: List<String>): Long {
    var sum = 0L

    for (range in ranges) {
        val numbers = range.split("-");

        for (i in numbers[0].toLong()..numbers[1].toLong()) {
            val numberAsString = i.toString();

            for (j in (1..numberAsString.length / 2)) {
                val sequence = numberAsString.substring(0, j);

                if (numberAsString.length % sequence.length == 0) {
                    var counter = 1;
                    for (k in (sequence.length..numberAsString.length - sequence.length) step sequence.length) {
                        if (numberAsString.substring(k, k + sequence.length) == sequence) {
                            counter++;
                        }
                    }

                    if (counter == numberAsString.length / sequence.length) {
                        sum += i
                        break
                    }
                }
            }
        }
    }

    return sum
}
