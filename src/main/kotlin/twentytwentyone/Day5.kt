package twentytwentyone

import harness.runProblem
import java.io.File
import java.lang.Integer.max

private fun main() = day5()

fun day5() {
    val day = "day_5"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 5) { part1(inputDemo) }
    runProblem("Part 1", expected = 7297) { part1(input) }
    runProblem("Part 2 (Demo)", expected = 12) { part2(inputDemo) }
    runProblem("Part 2", expected = 21038) { part2(input) }
}

private fun part2(input: List<String>): Int {
    val (intervals, width, height) = parseInput1(input)
    val field = Array(width * height) { 0 }

    for (interval in intervals) {
        interval.diagonalIndices { x, y ->
            val fieldIdx = intoIdx(x, y, width)
            field[fieldIdx] += 1
        }
    }

    return field.count { it > 1 }
}

private fun part1(input: List<String>): Int {
    val (intervals, width, height) = parseInput1(input)
    val field = Array(width * height) { 0 }

    for (interval in intervals) {
        interval.horizontalIndices { x, y ->
            val fieldIdx = intoIdx(x, y, width)
            field[fieldIdx] += 1
        }
    }

    return field.count { it > 1 }
}

private fun Interval.diagonalIndices(block: (x: Int, y: Int) -> Unit) {
    var x = this.x1
    var y = this.y1
    val xt = this.x2
    val yt = this.y2
    while (true) {
        block(x, y)
        if (x == xt && y == yt) break

        if (x < xt) {
            x += 1
        } else if (x > xt) {
            x -= 1
        }
        if (y < yt) {
            y += 1
        } else if (y > yt) {
            y -= 1
        }
    }
}

private fun Interval.horizontalIndices(block: (x: Int, y: Int) -> Unit) {
    if (this.x1 == this.x2) {
        val range = if (this.y1 <= this.y2) this.y1..this.y2 else this.y2..this.y1
        for (iy in range) {
            block(this.x1, iy)
        }
    }
    if (this.y1 == this.y2) {
        val range = if (this.x1 <= this.x2) this.x1..this.x2 else this.x2..this.x1
        for (ix in range) {
            block(ix, this.y1)
        }
    }
}

private fun intoIdx(col: Int, row: Int, rowLen: Int): Int = (row * rowLen) + col

data class Input(val intervals: List<Interval>, val width: Int, val height: Int)
data class Interval(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

private fun parseInput1(input: List<String>): Input {
    val intervals = mutableListOf<Interval>()
    var width = 0
    var height = 0
    for (line in input) {
        val twoParts = line.split(" -> ".toRegex())
        val entries = twoParts[0].split(",").map { it.toInt() }
        val exits = twoParts[1].split(",").map { it.toInt() }

        val x1 = entries[0]
        val y1 = entries[1]
        val x2 = exits[0]
        val y2 = exits[1]

        val interval = Interval(x1, y1, x2, y2)
        intervals.add(interval)

        width = max(entries[0], width)
        width = max(exits[0], width)
        height = max(entries[1], height)
        height = max(exits[1], height)
    }

    // 0, 9 -> width=10
    return Input(intervals, width + 1, height + 1)
}

@Suppress("unused")
private fun debug(message: String) {
    println(message)
}

@Suppress("unused")
private fun debugField(field: Array<Int>, width: Int, height: Int) {
    for (h in 0 until height) {
        for (w in 0 until width) {
            val idx = intoIdx(w, h, width)
            val number = field[idx]
            val block = if (number == 0) {
                "."
            } else {
                "$number"
            }
            val elem = block.padEnd(2)
            print(elem)
        }
        println()
    }
    println()
}
