package twentytwentyone

import harness.runProblem
import java.io.File

private fun main() = day11()

fun day11() {
    val day = "day_11"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 1656) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 195) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Long {
    val grid = parse(input)

    val total = (input.size * input[0].length).toLong()

    var step = 1L
    while (true) {
        grid.map { it + 1 }
        startFlash(grid)
        grid.map { if (it == -1) 0 else it }

        val count = grid.countEach { it == 0 }
        if (count == total) {
            break
        }

        step += 1
    }

    return step
}

private fun part1(input: List<String>): Long {
    val grid = parse(input)

    var flashes = 0L
    for (step in 0 until 100) {
        grid.map { it + 1 }
        startFlash(grid)
        flashes += grid.countEach { it == -1 }
        grid.map { if (it == -1) 0 else it }
    }

    return flashes
}

private fun startFlash(grid: Array<Array<Int>>) {
    for (iy in grid.indices) {
        val y = grid[iy]
        for (ix in y.indices) {
            if (y[ix] > 9) {
                flash(iy, ix, grid)
            }
        }
    }
}

private fun flash(y: Int, x: Int, grid: Array<Array<Int>>) {
    if (y < 0 || y >= grid.size) return
    if (x < 0 || x >= grid[0].size) return
    if (grid[y][x] == -1) return

    grid[y][x] += 1

    if (grid[y][x] > 9) {
        grid[y][x] = -1
        flash(y + 1, x, grid)
        flash(y - 1, x, grid)
        flash(y, x + 1, grid)
        flash(y, x - 1, grid)
        flash(y - 1, x - 1, grid)
        flash(y - 1, x + 1, grid)
        flash(y + 1, x - 1, grid)
        flash(y + 1, x + 1, grid)
    }
}

private fun Array<Array<Int>>.map(mapper: (Int) -> Int) {
    for (iy in this.indices) {
        val y = this[iy]
        for (ix in y.indices) {
            y[ix] = mapper(y[ix])
        }
    }
}

private fun Array<Array<Int>>.countEach(mapper: (Int) -> Boolean): Long {
    var count = 0L
    for (iy in this.indices) {
        val y = this[iy]
        for (ix in y.indices) {
            if (mapper(y[ix])) {
                count +=1
            }
        }
    }

    return count
}

private fun parse(input: List<String>): Array<Array<Int>> {
    val output = Array(input.size) { Array(input[0].length) { -1000 } }
    for (lineIdx in input.indices) {
        output[lineIdx] = input[lineIdx].split("").filter { it.isNotEmpty() }.map { it.toInt() }.toTypedArray()
    }

    return output
}

@Suppress("unused")
private fun Array<Array<Int>>.debug(message: String = "") {
    println(message)
    for (y in this) {
        for (x in y) {
            print("$x".padStart(4))
        }
        println()
    }
    println()
}

@Suppress("unused")
private fun checkContent(grid: Array<Array<Int>>, file: String) {
    val correctGrid = parse(File("inputs/2021/$file").readLines())

    var differences = 0
    for (iy in grid.indices) {
        for (ix in grid[iy].indices) {
            if (grid[iy][ix] != correctGrid[iy][ix]) {
            differences++
            }
        }
    }

    if (differences == 0) {
        println("Content equals to $file")
        return
    }

    println("Found differences in $file")

    for (iy in grid.indices) {
        for (ix in grid[iy].indices) {
            val content = if (grid[iy][ix] != correctGrid[iy][ix]) {
                "${grid[iy][ix]}|${correctGrid[iy][ix]}".padStart(4)
            } else {
                "${grid[iy][ix]}".padStart(4)
            }
            print("$content ")
        }
        println()
    }
    println()

    throw RuntimeException("Content does not match $file | differences:$differences")
}