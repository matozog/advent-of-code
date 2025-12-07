package aoc2025.day4

import commons.FileUtils

fun main() {
    val fileLines = FileUtils.getFileLinesAsStringByDelimiter("src/main/kotlin/aoc2025/day4/input.txt", "\n");

    val rows = fileLines.split("\n");
    val map = Array(rows.size) { Array(rows[0].length) { "" } }
    var tmpRow: List<String>;

    for (i in rows.indices) {
        tmpRow = rows[i].split("").filter { it.isNotEmpty() };
        for (j in tmpRow.indices) {
            map[i][j] = tmpRow[j]
        }
    }

    println("Part 1: " + part1(map))
    println("Part 2: " + part2(map))
}

class Point(var x: Int, var y: Int)

fun getCellNeighbours(x: Int, y: Int, map: Array<Array<String>>): List<String> {
    val neighbours: List<Point> = listOf(
        Point(x - 1, y),
        Point(x + 1, y),
        Point(x - 1, y - 1),
        Point(x, y - 1),
        Point(x + 1, y - 1),
        Point(x - 1, y + 1),
        Point(x, y + 1),
        Point(x + 1, y + 1)
    );

    return neighbours.filter { it.y >= 0 && it.x >= 0 && it.y < map.size && it.x < map[y].size }.map { map[it.y][it.x] }
}

fun part1(map: Array<Array<String>>): Int {
    var accessibleRolls = 0
    var neighbours: List<String>

    for (i in map.indices) {
        for (j in map[0].indices) {
            if (map[i][j] == "@") {
                neighbours = getCellNeighbours(j, i, map);
                if (neighbours.filter { it == "@" }.size < 4) {
                    accessibleRolls++
                }
            }
        }
    }

    return accessibleRolls;
}

fun part2(map: Array<Array<String>>): Int {
    val accessibleRolls: MutableList<Point> = mutableListOf()
    var sumOfAccessibleRolls = 0
    var neighbours: List<String>

    do {
        accessibleRolls.clear()
        for (i in map.indices) {
            for (j in map[0].indices) {
                if (map[i][j] == "@") {
                    neighbours = getCellNeighbours(j, i, map);
                    if (neighbours.filter { it == "@" }.size < 4) {
                        accessibleRolls.add(Point(j, i))
                    }
                }
            }
        }
        sumOfAccessibleRolls += accessibleRolls.size

        accessibleRolls.forEach({ map[it.y][it.x] = "." });
    } while (accessibleRolls.size > 0)

    return sumOfAccessibleRolls;
}