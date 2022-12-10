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
    runProblem("Part 2 (Demo)", expected = 8) { part2(inputDemo) }
    runProblem("Part 2", expected = 444528) { part2(input) }
}

private fun part2(input: List<String>): Int {
    val grid = parse(input)

    var max = -1
    for (h in 0 until grid.height) {
        for (w in 0 until grid.width) {
            val up = scoreUp(grid, w, h)
            val down = scoreDown(grid, w, h)
            val right = scoreRight(grid, w, h)
            val left = scoreLeft(grid, w, h)

            val score = up * down * right * left
            if (score > max) max = score
        }
    }

    return max
}

private fun scoreLeft(grid: Grid, cx: Int, cy: Int): Int {
    val left = grid.left(cx, cy, inclusive = false)
    var max: Int? = null
    var count = 0
    for (x in left) {
        val c = grid.at(x, cy)
        if (max == null) {
            max = c
        } else if (c < max) {
            count++
        } else if (c >= max) {
            count++
            break
        }
    }

    return count
}

private fun scoreRight(grid: Grid, cx: Int, cy: Int): Int {
    val right = grid.right(cx, cy, inclusive = false)
    var max: Int? = null
    var count = 0
    for (x in right) {
        val c = grid.at(x, cy)
        if (max == null) {
            max = c
        } else if (c < max) {
            count++
        } else if (c >= max) {
            count++
            break
        }
    }

    return count
}


private fun scoreUp(grid: Grid, cx: Int, cy: Int): Int {
    val up = grid.up(cx, cy, inclusive = false)
    var max: Int? = null
    var count = 0
    for (y in up) {
        val c = grid.at(cx, y)
        if (max == null) {
            max = c
        } else if (c < max) {
            count++
        } else if (c >= max) {
            count++
            break
        }
    }

    return count
}

private fun scoreDown(grid: Grid, x: Int, y: Int): Int {
    val down = grid.down(x, y, inclusive = false)
    var max: Int? = null
    var count = 0
    for (y in down) {
        val c = grid.at(x, y)
        if (max == null) {
            max = c
        } else if (c < max) {
            count++
        } else if (c >= max) {
            count++
            break
        }
    }

    return count
}

private fun part1(input: List<String>): Int {
    val grid = parse(input)
    val visible = HashSet<Pair<Int, Int>>()

    for (h in 0 until grid.height) {
        var max = -1
        for (x in 0 until grid.width) {
            val curr = grid.at(x, h)
            if (curr > max) {
                max = curr
                visible.add(x to h)
            }
        }
        max = -1
        for (x in (grid.width - 1) downTo 0) {
            val curr = grid.at(x, h)
            if (curr > max) {
                max = curr
                visible.add(x to h)
            }
        }
    }
    for (w in 0 until grid.width) {
        var max = -1
        for (y in 0 until grid.height) {
            val curr = grid.at(w, y)
            if (curr > max) {
                max = curr
                visible.add(w to y)
            }
        }
        max = -1
        for (y in (grid.height - 1) downTo 0) {
            val curr = grid.at(w, y)
            if (curr > max) {
                max = curr
                visible.add(w to y)
            }
        }
    }

    return visible.size
}


typealias Grid = Array<Array<Int>>

val Grid.width: Int
    get() = get(0).size
val Grid.height: Int
    get() = size

fun Grid.at(x: Int, y: Int) = get(y)[x]

fun Grid.atOrNull(x: Int, y: Int): Int? {
    return if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
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


fun Grid.up(x: Int, y: Int, inclusive: Boolean = true) = (y downTo 0)
fun Grid.down(x: Int, y: Int, inclusive: Boolean = true) = (y until height)

fun Grid.left(x: Int, y: Int, inclusive: Boolean = true) = (x downTo 0)
fun Grid.right(x: Int, y: Int, inclusive: Boolean = true) = (x until width)


private fun parse(input: List<String>): Grid {
    return Array(input.size) {
        input[it].map { mapped -> mapped.toString().toInt() }.toTypedArray()
    }
}