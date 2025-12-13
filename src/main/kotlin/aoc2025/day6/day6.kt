package aoc2025.day6

import commons.FileUtils

fun main() {
    val fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/kotlin/aoc2025/day6/input.txt", "\n");

    val rows = fileLines.split("\n")

    val operationRegex = Regex("""[+*]""")
    val operations = operationRegex.findAll(rows[rows.size - 1]).map { it.value }.toList()


    println("Part 1: " + part1(rows.subList(0, rows.size - 1), operations))
    println("Part 2: " + part2(rows.subList(0, rows.size - 1), operations))
}

class Problem(val numbers: MutableList<Long>, var operation: String)

fun part1(rows: List<String>, operations: List<String>): Long {
    var sum = 0L

    val numbersRegex = Regex("""\d+""")
    val mathProblems: MutableList<Problem> = mutableListOf()

    for (i in rows.indices) {
        val numbers = numbersRegex.findAll(rows[i]).map { it.value.toLong() }.toList();
        for (j in numbers.indices) {
            if (i == 0) {
                mathProblems.add(Problem(MutableList(rows.size) { 0 }, ""))
            }
            mathProblems[j].numbers[i] = numbers[j]
        }
    }

    for (i in operations.indices) {
        mathProblems[i].operation = operations[i];
    }

    for (problem in mathProblems) {
        var tmpSum = 0L
        if (problem.operation == "*") {
            tmpSum = 1L
            problem.numbers.forEach { value -> tmpSum *= value }
        } else {
            problem.numbers.forEach { value -> tmpSum += value }
        }

        sum += tmpSum;
    }

    return sum
}

fun calculateValue(numbers: MutableList<Long>, operation: String): Long {
    return if (operation == "+") {
        numbers.sum()
    } else {
        numbers.fold(1L) { acc, value ->
            acc * value
        }
    }
}

fun part2(rows: List<String>, operations: List<String>): Long {
    var sum = 0L
    val numbers: MutableList<Long> = mutableListOf()
    var number = ""
    var counter = 0

    for (j in 0..<rows[0].length) {
        for (element in rows) {
            number += element[j]
        }
        number = number.trim()
        if (number.isNotEmpty()) {
            numbers.add(number.toLong())
            if (j == rows[0].length - 1) {
                sum += calculateValue(numbers, operations[counter])
            }
        } else if (number.isEmpty()) {
            sum += calculateValue(numbers, operations[counter])
            counter++
            numbers.clear()
        }
        number = ""
    }

    return sum
}