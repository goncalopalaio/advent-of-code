import Direction.*
import harness.runProblem
import java.io.File
import kotlin.math.abs
import kotlin.math.exp

private fun main() = day9()

fun day9() {
    val day = "day_9"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val inputDemo2 = File("inputs/2022/${day}_demo_2.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 13) { part1(inputDemo) }
    runProblem("Part 1", expected = 6357) { part1(input) }
    runProblem("Part 2 (Demo 1)", expected = 1) { part2(inputDemo) }
    runProblem("Part 2 (Demo 2)", expected = 36) { part2(inputDemo2) }
    runProblem("Part 2", expected = 2627) { part2(input) }
}

private fun part2(input: List<String>): Int {
    val moves = parse(input)
    val r = Ropes(9, 11, 15, 11, 15)

    val tailPositions = HashSet<Pair<Int, Int>>()
    for ((direction, ticks) in moves) {
        for (t in 0 until ticks) {
            when (direction) {
                Up -> r.move(0, -1)
                Down -> r.move(0, 1)
                Right -> r.move(1, 0)
                Left -> r.move(-1, 0)
            }
            r.updateTail()
            tailPositions.add(r.tail())
        }
    }

    return tailPositions.size
}

private fun part1(input: List<String>): Int {
    val moves = parse(input)

    with(Rope(2, 1, 1, 1)) {
        move(1, 0)
        assert(3, 1, 1, 1)
        updateTail()
        assert(3, 1, 2, 1)
    }

    with(Rope(1, 2, 1, 1)) {
        move(0, 1)
        updateTail()
        assert(1, 3, 1, 2)
    }

    with(Rope(2, 2, 1, 3)) {
        move(0, -1)
        updateTail()
        assert(2, 1, 2, 2)
    }

    with(Rope(2, 2, 1, 3)) {
        move(1, 0)
        updateTail()
        assert(3, 2, 2, 2)
    }

    with(Rope(0, 0, 0, 0)) {
        move(1, 0)
        updateTail()
        assert(1, 0, 0, 0)
    }

    val r = Rope()
    val tailPositions = HashSet<Pair<Int, Int>>()
    for ((direction, ticks) in moves) {
        for (t in 0 until ticks) {
            when (direction) {
                Up -> r.move(0, -1)
                Down -> r.move(0, 1)
                Right -> r.move(1, 0)
                Left -> r.move(-1, 0)
            }
            r.updateTail()
            tailPositions.add(r.tail())
        }
    }

    return tailPositions.size
}

class Ropes(n: Int = 10, ixh: Int = 0, iyh: Int = 0, ixt: Int = 0, iyt: Int = 0) {
    private var xh = ixh
    private var yh = iyh

    private val knots = Array(n) {
        Pair(ixt, iyt)
    }

    fun updateTail() {
        var pxh = xh
        var pyh = yh
        for ((i, k) in knots.withIndex()) {
            val nt = updateTail(pxh, pyh, k.x, k.y)
            knots[i] = nt
            pxh = nt.x
            pyh = nt.y
        }
    }

    private fun updateTail(cxh: Int, cyh: Int, cxt: Int, cyt: Int): Pair<Int, Int> {
        var mx = cxh - cxt
        var my = cyh - cyt

        if (abs(mx) >= 2 || abs(my) >= 2) {
            mx = if (mx == -2) -1 else if (mx == 2) 1 else mx
            my = if (my == -2) -1 else if (my == 2) 1 else my
        } else {
            mx = 0
            my = 0
        }

        return (cxt + mx) to (cyt + my)
    }

    fun move(dx: Int, dy: Int) {
        xh += dx
        yh += dy
    }


    fun tail(): Pair<Int, Int> {
        val last = knots.last()
        return last.x to last.y
    }

    fun printGrid(w: Int, h: Int) {
        val grid = emptyGrid(w, h)
        grid.set(xh, yh, 99)

        for ((i, k) in knots.withIndex()) {
            grid.set(k.x, k.y, i + 1)
        }

        grid.print()
    }
}

class Rope(ixh: Int = 0, iyh: Int = 0, ixt: Int = 0, iyt: Int = 0) {
    private var xh = ixh
    private var yh = iyh

    private var xt = ixt
    private var yt = iyt

    fun updateTail() {
        var mx = xh - xt
        var my = yh - yt

        if (abs(mx) >= 2 || abs(my) >= 2) {
            mx = if (mx == -2) -1 else if (mx == 2) 1 else mx
            my = if (my == -2) -1 else if (my == 2) 1 else my
        } else {
            mx = 0
            my = 0
        }


        xt += mx
        yt += my
    }

    fun move(dx: Int, dy: Int) {
        xh += dx
        yh += dy
    }

    fun tail() = xt to yt

    fun assert(cxh: Int, cyh: Int, cxt: Int, cyt: Int) {
        if (cxh != xh || cyh != yh || cxt != xt || cyt != yt) {
            error("($cxh, $cyh)-($cxt, $cyt) -> ($xh, $yh)-($xt, $yt)")
        }
    }
}

enum class Direction {
    Up,
    Down,
    Right,
    Left,
}

val Pair<Int, Int>.x: Int
    get() = first

val Pair<Int, Int>.y: Int
    get() = second

private fun parse(input: List<String>): List<Pair<Direction, Int>> {
    val result = ArrayList<Pair<Direction, Int>>()

    for (line in input) {
        val (letter, number) = line.split(" ")
        val direction = when (letter) {
            "R" -> Right
            "U" -> Up
            "L" -> Left
            "D" -> Down
            else -> error("Unexpected input | $line")
        }
        val quantity = number.toInt()
        result.add(direction to quantity)
    }

    return result
}