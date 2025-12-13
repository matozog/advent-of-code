package aoc2025.day5

import commons.FileUtils

fun main() {
    val fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/kotlin/aoc2025/day5/input.txt", "\n");

    val rows = fileLines.split("\n")
    val ranges: MutableList<Range> = mutableListOf()
    val products: MutableList<Long> = mutableListOf()
    var addRanges = true;

    rows.forEach {
        if (it.isEmpty()) {
            addRanges = false;
        }

        if (addRanges) {
            val range = it.split('-')
            ranges.add(Range(range[0].toLong(), range[1].toLong()))
        } else if (it.isNotEmpty()) {
            products.add(it.toLong())
        }
    }

    println("Part 1: " + part1(ranges, products))
    println("Part 2: " + part2(ranges))
}

class Range(val start: Long, val stop: Long) {
    fun isInRange(number: Long): Boolean {
        return number in start..stop;
    }
}

fun part1(ranges: MutableList<Range>, products: MutableList<Long>): Long {
    var sum = 0L;

    products.forEach { product ->
        val range = ranges.firstOrNull { it.isInRange(product) }
        if (range != null) {
            sum++
        }
    }

    return sum;
}

fun part2(ranges: MutableList<Range>): Long {
    var sum = 0L;
    val sortedRanges = ranges.sortedBy { it.start }
    var prevRange: Range = Range(0, 0)

    sortedRanges.forEach {
        sum += if (prevRange.isInRange(it.start) && prevRange.isInRange(it.stop)) {
            return@forEach
        } else if (prevRange.isInRange(it.start)) {
            it.stop - prevRange.stop
        } else {
            it.stop - it.start + 1
        }
        prevRange = it
    }

    return sum;
}