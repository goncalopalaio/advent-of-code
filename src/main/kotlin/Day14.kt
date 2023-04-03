import harness.runProblem
import java.io.File
import kotlin.math.max
import kotlin.math.min

private fun main() = day14()

private const val AIR = 0
private const val ROCK = 1
private const val SAND = 2
val POURING = 500 to 0

fun day14() {
    val day = "day_14"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)") { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)") { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    val coordinates = parse(input)
    val all = coordinates.flatten()
    val maxX = (all.maxOf { it.first } + 1) * 2
    val maxY = all.maxOf { it.second } + 2

    val grid: Grid = Array(maxY + 4) {
        Array(maxX) {
            AIR
        }
    }

    fillRocks(grid, coordinates)
    for (c in 0 until maxX) {
        grid.set(c, maxY, ROCK)
    }
    // grid.printCave()

    var i = -1
    while (true) {
        i++
        try {
            stepVersionTwo(maxY, grid, POURING.x, POURING.y)
            // grid.printCave()
        } catch (e: OutOfBoundsException) {
            return i + 1
        }
    }
    return 0
}

private fun part1(input: List<String>): Int {
    val coordinates = parse(input)
    val all = coordinates.flatten()
    val maxX = all.maxOf { it.first } + 1
    val maxY = all.maxOf { it.second } + 1

    val grid: Grid = Array(maxY) {
        Array(maxX) {
            AIR
        }
    }

    fillRocks(grid, coordinates)
    // grid.printCave()

    val pouring = 500 to 0
    var i = -1
    while (true) {
        i++
        try {
            stepVersionOne(maxY, grid, pouring.x, pouring.y)
            // grid.printCave()
        } catch (e: OutOfBoundsException) {
            return i
        }
    }
}

class OutOfBoundsException : Exception()

private fun stepVersionOne(maxY: Int, grid: Grid, x: Int, y: Int) {
    if (y + 1 >= maxY) throw OutOfBoundsException()

    if (grid.at(x, y + 1) == AIR) {
        return stepVersionOne(maxY, grid, x, y + 1)
    }
    val diagonalLeft = grid.at(x - 1, y + 1)
    if (diagonalLeft == AIR) {
        return stepVersionOne(maxY, grid, x - 1, y + 1)
    }
    val diagonalRight = grid.at(x + 1, y + 1)
    if (diagonalRight == AIR) {
        return stepVersionOne(maxY, grid, x + 1, y + 1)
    }

    grid.set(x, y, SAND)
}

private fun stepVersionTwo(maxY: Int, grid: Grid, x: Int, y: Int) {
    val down = grid.atOrNull(x, y + 1)
    if (down == null) {
        if (grid.at(x, y) == AIR) {
            grid.set(x, y, SAND)
        }
        return
    }
    if (down == AIR) {
        return stepVersionTwo(maxY, grid, x, y + 1)
    }
    val diagonalLeft = grid.atOrNull(x - 1, y + 1)
    if (diagonalLeft == null) {
        if (grid.at(x, y) == AIR) {
            grid.set(x, y, SAND)
        }
        return
    }
    if (diagonalLeft == AIR) {
        return stepVersionTwo(maxY, grid, x - 1, y + 1)
    }
    val diagonalRight = grid.atOrNull(x + 1, y + 1)
    if (diagonalRight == null) {
        if (grid.at(x, y) == AIR) {
            grid.set(x, y, SAND)
        }
        return
    }
    if (diagonalRight == AIR) {
        return stepVersionTwo(maxY, grid, x + 1, y + 1)
    }

    if (x == POURING.first && y == POURING.second) throw OutOfBoundsException()
    grid.set(x, y, SAND)
}

private fun parse(input: List<String>): List<List<Pair<Int, Int>>> {
    val output = ArrayList<ArrayList<Pair<Int, Int>>>()
    for (line in input) {
        val p = line.split(" -> ")
        val r = ArrayList<Pair<Int, Int>>()
        for (c in p) {
            val (x, y) = c.split(",")
            r.add(x.toInt() to y.toInt())
        }
        output.add(r)
    }

    return output
}

fun fillRocks(grid: Grid, coordinates: List<List<Pair<Int, Int>>>) {
    for (line in coordinates) {
        for (i in 1 until line.size) {
            val (x1, y1) = line[i - 1]
            val (x2, y2) = line[i]

            if (x1 == x2) {
                val s = min(y1, y2)
                val e = max(y1, y2)
                for (c in s..e) {
                    grid.set(x1, c, ROCK)
                }
            } else if (y1 == y2) {
                val s = min(x1, x2)
                val e = max(x1, x2)
                for (c in s..e) {
                    grid.set(c, y1, ROCK)
                }
            } else {
                error("Non horizontal line?")
            }
        }
    }

}

fun Grid.printCave(padding: Int = 0) {
    for (h in 0 until height) {
        for (w in 0 until width) {
            val block = when (val v = at(w, h)) {
                ROCK -> "#"
                AIR -> "."
                SAND -> "o"
                else -> error("Unexpected value $v")
            }
            print(block.padEnd(padding))
        }
        println()
    }
}
