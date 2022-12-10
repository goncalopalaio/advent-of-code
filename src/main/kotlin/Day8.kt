import harness.runProblem
import java.io.File

private fun main() = day8()

fun day8() {
    val day = "day_8"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    val test1 = listOf(
        "111",
        "121",
        "111",
    )
    val test2 = listOf(
        "11211",
        "12121",
        "11211",
    )
    runProblem("Part 1 (Test 1)", expected = 9) { part1(test1) }
    runProblem("Part 1 (Test 2)", expected = 14) { part1(test2) }
    runProblem("Part 1 (Demo)", expected = 21) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    // runProblem("Part 2 (Demo)") { part2(inputDemo) }
    // runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    return 0
}

private fun part1(input: List<String>): Int {
    val grid = parse(input)
    val visible = HashSet<Pair<Int, Int>>()
    val visitedGrid = emptyGrid(grid.width, grid.height)

    for (h in 0 until grid.height) {
        for (x in 0 until grid.width) {
            val curr = grid.at(x, h)
        }
        for (x in (grid.width - 1) downTo 0) {
            val curr = grid.at(x, h)
        }
    }
    for (w in 0 until grid.width) {
        for (y in 0 until grid.height) {
            val curr = grid.at(w, y)
        }
        for (y in (grid.height - 1) downTo 0) {
            val curr = grid.at(w, y)
        }
    }

    grid.print()
    for (v in visible) {
        val (x,y) = v
        val value = grid.at(x,y)
    }
    return 0
}


typealias Grid = Array<Array<Int>>

val Grid.width: Int
    get() = get(0).size
val Grid.height: Int
    get() = size

fun Grid.at(x: Int, y: Int) = get(y)[x]

fun Grid.atOrNull(x: Int, y: Int): Int? {
    return if ((x < 0)
        || (x >= width)
        || (y < 0)
        || (y >= height)
    ) {
        null
    } else {
        get(y)[x]
    }
}

fun Grid.set(x: Int, y: Int, value: Int) {
    get(y)[x] = value
}

fun emptyGrid(x: Int, y: Int) = Array(y) {
    Array(x) { 0 }
}

fun Grid.print() {
    for (h in 0 until height) {
        for (w in 0 until width) {
            print("${at(w, h)}")
        }
        println()
    }
}

private fun parse(input: List<String>): Grid {
    return Array(input.size) {
        input[it].map { mapped -> mapped.toString().toInt() }.toTypedArray()
    }
}