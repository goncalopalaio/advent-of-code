import java.io.File

private fun main() = day9()

fun day9() {
    val day = "day_9"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 15) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 1134) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    val grid = parse(input)

    val h = grid.size
    val w = grid[0].size

    val basinSizes = mutableListOf<Int>()
    for (y in 0 until h) {
        for (x in 0 until w) {
            val center = pokeGrid(y, x, grid, h, w)!!
            val up = pokeGrid(y - 1, x, grid, h, w)
            val down = pokeGrid(y + 1, x, grid, h, w)
            val left = pokeGrid(y, x - 1, grid, h, w)
            val right = pokeGrid(y, x + 1, grid, h, w)

            val surrounding = listOfNotNull(up, down, left, right).minOrNull()!!

            if (center < surrounding) {
                val newGrid = grid.copyOf()
                newGrid[y][x] = 9
                val basin = HashSet<Pair<Int, Int>>()
                floodFill(y - 1, x, newGrid, h, w, basin)
                floodFill(y + 1, x, newGrid, h, w, basin)
                floodFill(y, x - 1, newGrid, h, w, basin)
                floodFill(y, x + 1, newGrid, h, w, basin)

                basinSizes.add(basin.size + 1)
            }
        }
    }

    basinSizes.sort()
    basinSizes.reverse()

    return basinSizes[0] * basinSizes[1] * basinSizes[2]
}

private fun floodFill(
    y: Int,
    x: Int,
    grid: Array<IntArray>,
    h: Int,
    w: Int,
    elements: HashSet<Pair<Int, Int>>
) {
    val center = pokeGrid(y, x, grid, h, w) ?: return
    if (center == 9) return

    val centerPos = Pair(y, x)
    elements.add(centerPos)
    grid[y][x] = 9

    floodFill(y - 1, x, grid, h, w, elements)
    floodFill(y + 1, x, grid, h, w, elements)
    floodFill(y, x - 1, grid, h, w, elements)
    floodFill(y, x + 1, grid, h, w, elements)
}

private fun part1(input: List<String>): Int {
    val grid = parse(input)

    val h = grid.size
    val w = grid[0].size

    var riskLevel = 0
    for (y in 0 until h) {
        for (x in 0 until w) {
            val center = pokeGrid(y, x, grid, h, w)!!
            val up = pokeGrid(y - 1, x, grid, h, w)
            val down = pokeGrid(y + 1, x, grid, h, w)
            val left = pokeGrid(y, x - 1, grid, h, w)
            val right = pokeGrid(y, x + 1, grid, h, w)

            val surrounding = listOfNotNull(up, down, left, right).minOrNull()!!

            if (center < surrounding) {
                riskLevel += center + 1
            }
        }
    }

    return riskLevel
}

private fun pokeGrid(y: Int, x: Int, grid: Array<IntArray>, h: Int, w: Int): Int? {
    if (y < 0 || y >= h) return null
    if (x < 0 || x >= w) return null
    return grid[y][x]
}

private fun parse(input: List<String>): Array<IntArray> {
    val output = Array(input.size) { IntArray(input[0].length) { -1 } }

    for (lineIdx in input.indices) {
        val line = input[lineIdx]
        val numbers = line.split("").filter { it.isNotBlank() }.map { it.toInt() }

        for (n in numbers.indices) {
            output[lineIdx][n] = numbers[n]
        }
    }
    return output
}