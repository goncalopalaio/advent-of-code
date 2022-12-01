package twentytwentyone

import runProblem
import java.io.File

private const val DEBUG = false

private fun main() = day4()

fun day4() {
    val day = "day_4"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 4512) { part1(inputDemo) }
    runProblem("Part 1", expected = 51776) { part1(input) }
    runProblem("Part 2 (Demo)", expected = 1924) { part2(inputDemo) }
    runProblem("Part 2", expected = 16830) { part2(input) }
}

class Board(private val numbers: Array<Int>) {
    private val field = Array(5 * 5) { false }

    init {
        if (numbers.size != (5 * 5)) throw RuntimeException("Unexpected input")
    }

    fun set(number: Int): Pair<Int?, Int?> {
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                val idx = intoIdx(i, j)
                if (numbers[idx] == number) {
                    field[idx] = true
                    val isFinished = isFinished(i, j)

                    debugBoard()
                    debug("isFinished=$isFinished")
                    return isFinished
                }
            }
        }

        debugBoard()
        return Pair(null, null)
    }

    fun unmarkedSum(): Int {
        var sum = 0
        numbers.forEachIndexed { index, number ->
            if (!field[index]) sum += number
        }
        return sum
    }

    private fun isFinished(changedRow: Int, changedCol: Int): Pair<Int?, Int?> {
        var hCount = 0
        var vCount = 0
        for (idx in 0 until 5) {
            val h = intoIdx(changedRow, idx)
            val v = intoIdx(idx, changedCol)

            if (field[h]) {
                hCount += 1

                if (hCount >= 5) {
                    return Pair(changedRow, null)
                }
            }

            if (field[v]) {
                vCount += 1

                if (vCount >= 5) {
                    return Pair(null, changedCol)
                }
            }
        }

        return Pair(null, null)
    }

    private fun intoIdx(row: Int, col: Int, rowLen: Int = 5): Int {
        return (row * rowLen) + col
    }

    private fun debugBoard() {
        if (!DEBUG) return

        var spacing = 1

        numbers.forEachIndexed { index, b ->
            val element = if (field[index]) {
                "$b*"
            } else {
                "$b"
            }
            print(element.padEnd(4))

            if (spacing % 5 == 0) {
                print("_".padEnd(4))
            }
            spacing += 1
        }
        println()
    }
}

private fun part2(input: List<String>): Int {
    val (drawnNumbers, originalBoards) = parseInput(input)

    val boards = originalBoards.toMutableList()
    var lastDrawnNumber = 0
    var lastSolution: Pair<Int?, Int?>? = null
    var lastBoard: Board? = null
    for (drawn in drawnNumbers) {
        debug("drawn=$drawn")
        val iterator = boards.iterator()
        while (iterator.hasNext()) {
            val board = iterator.next()
            val solution = board.set(drawn)
            if (solution.first != null || solution.second != null) {
                lastDrawnNumber = drawn
                lastSolution = solution
                lastBoard = board
                iterator.remove()
            }
        }
    }

    debug("lastDrawnNumber=$lastDrawnNumber, lastSolution=$lastSolution")
    if (lastSolution!!.first != null || lastSolution.second != null) {
        return lastDrawnNumber * lastBoard!!.unmarkedSum()
    }

    return 0
}

private fun part1(input: List<String>): Int {
    val (drawnNumbers, boards) = parseInput(input)

    for (drawn in drawnNumbers) {
        debug("drawn=$drawn")
        for (board in boards) {
            val solution = board.set(drawn)
            if (solution.first != null || solution.second != null) {
                return drawn * board.unmarkedSum()
            }
        }
    }

    return 0
}

private fun parseInput(input: List<String>): Pair<List<Int>, List<Board>> {
    val drawn = input[0].split(",").map { it.toInt() }
    val boards = mutableListOf<Board>()
    var nBoards = 0
    var lineIdx = 2
    while (lineIdx < input.size) {
        val board = Array(5 * 5) { -1 }
        for (boardIdx in 0 until 5) {
            val boardLine = input[lineIdx].split("\\s+".toRegex()).filter { it != "" }.map { it.toInt() }
            boardLine.forEachIndexed { index, s ->
                board[index + (boardIdx * 5)] = s
            }
            lineIdx += 1
        }
        nBoards += 1
        lineIdx += 1

        boards.add(Board(board))
    }

    return Pair(drawn, boards)
}

private fun debug(message: String) {
    @Suppress("ConstantConditionIf")
    if (DEBUG) println(message)
}