import java.io.File
import kotlin.math.max

private fun main() = day13()

fun day13() {
    val day = "day_13"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 17) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)") { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Long {
    val (paper, instructions) = parse(input)

    var h = paper.size
    var w = paper[0].size
    println("h:$h, w:$w")

    for (intr in instructions) {
        val (nh, nw) = paper.paperFold(intr, h, w)
        h = nh
        w = nw
        println("h:$h, w:$w")
        println()
    }

    println("Final: h:$h, w:$w")
    debug(paper, h, w)
    println()
    println()
    return 0
}

private fun part1(input: List<String>): Long {
    val (paper, instructions) = parse(input)

    val h = paper.size
    val w = paper[0].size
    println("h:$h, w:$w")

    for (inst in instructions) {
        paper.paperFold(inst, h, w)
        break
    }

    return paper.visible()
}

private fun Array<Array<Int>>.visible(): Long {
    var count = 0L
    for (y in 0 until this.size) {
        for (x in 0 until this[0].size) {
            if (this[y][x] > 0) {
                count++
            }
        }
    }
    return count
}

private fun Array<Array<Int>>.paperFold(fold: Fold, h: Int, w: Int): Pair<Int, Int> {
    return when (fold) {
        is Fold.Left -> foldLeft(this, fold.value, h, w)
        is Fold.Up -> foldUp(this, fold.value, h ,w)
    }
}

private fun foldUp(paper: Array<Array<Int>>, value: Int, h: Int, w: Int, clean: Boolean = true): Pair<Int, Int> {
    val yRange = (value - 1) downTo 0
    val xRange = 0 until w
    println("value:$value")
    println("h:$h U: yRange=$yRange")
    println("w:$w U: xRange=$xRange")
    for (ty in yRange) {
        val sy = (value + 1) + ((value - 1) - ty)
        if (sy >= h) continue
        for (x in xRange) {
            paper[ty][x] += paper[sy][x]
            if (clean) paper[sy][x] = 0
        }
    }

    return Pair(value, w)
}

private fun foldLeft(paper: Array<Array<Int>>, value: Int, h: Int, w: Int, clean: Boolean = true): Pair<Int, Int> {
    val yRange = 0 until h
    val xRange = (value - 1) downTo 0
    println("value:$value")
    println("h:$h L: yRange=$yRange")
    println("w:$w L: xRange=$xRange")
    for (y in yRange) {
        for (dx in xRange) {
            val sx = (value + 1) + ((value - 1) - dx)
            paper[y][dx] += paper[y][sx]
            if (clean) paper[y][sx] = 0
        }
    }

    return Pair(h, value)
}

private fun debug(paper: Array<Array<Int>>, h: Int, w: Int, showNumbers: Boolean = false) {
    println()
    println()
    for (ty in 0 until h) {
        for (tx in 0 until w) {
            val char = if (showNumbers) {
                "${paper[ty][tx]}"
            } else {
                if (paper[ty][tx] >= 1) {
                    "#"
                } else {
                    " "
                }
            }

            print(char)
        }
        println()
    }
}

private data class Point(val y: Int, val x: Int)

private sealed class Fold {
    class Up(val value: Int) : Fold() {
        override fun toString(): String {
            return "Up(value=$value)"
        }
    }

    class Left(val value: Int) : Fold() {
        override fun toString(): String {
            return "Left(value=$value)"
        }
    }
}

private fun parse(input: List<String>): Pair<Array<Array<Int>>, List<Fold>> {
    var xMax = 0
    var yMax = 0
    val instructionsStart = input.indexOf("")
    val points = mutableListOf<Pair<Int, Int>>()
    //println("Paper:")
    for (idx in 0 until instructionsStart) {
        val line = input[idx]
        val (xs, ys) = line.split(",")
        val x = xs.toInt()
        val y = ys.toInt()
        points.add(Pair(y, x))
        xMax = max(xMax, x)
        yMax = max(yMax, y)
        //println(line)
    }

    val paper = Array(yMax + 1) { Array(xMax + 1) { 0 } }
    for (p in points) {
        paper[p.first][p.second] = 1
    }

    val foldAlongY = "fold along y="
    val foldAlongX = "fold along x="
    //println("Instructions:")
    val instructions = mutableListOf<Fold>()
    for (idx in (instructionsStart + 1) until input.size) {
        val line = input[idx]
        if (line.startsWith(foldAlongY)) {
            instructions.add(Fold.Up(line.substring(foldAlongY.length).toInt()))
        } else if (line.startsWith(foldAlongX)) {
            instructions.add(Fold.Left(line.substring(foldAlongX.length).toInt()))
        } else {
            throw RuntimeException("Unexpected value: $line")
        }
        //println(line)
    }
    println("$instructions")
    return Pair(paper, instructions)
}