package twentytwentyone

import harness.runProblem
import java.io.File
import java.util.*
import kotlin.Comparator
import kotlin.math.abs

private fun main() = day15()

fun day15() {
    val day = "day_15"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)") { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 315) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun value2(oh: Int, ow: Int, grid: Array<Array<Int>>): (Int, Int) -> Int {
    return { y, x ->
        val multY = (y / oh)
        val my = y % oh
        val multX = (x / ow)
        val mx = x % ow

        var value = grid[my][mx] + multX + multY
        if (value != 9) {
            value %= 9
        }

        if (value == 0) value = 1
        value
    }
}

private fun heuristic2(): (Int, Int, Int, Int) -> Float {
    return { y, x, endY, endX ->
        // euclidean squared - (((y - endY).toDouble()).pow(2.0) + ((x - endX).toDouble()).pow(2.0)).toFloat()
        // manhattan distance - abs(endY - y) + abs(endX - x)
        (abs(endY - y) + abs(endX - x)).toFloat()
    }
}


private fun part2(input: List<String>): Long {
    val grid = parse(input)
    // grid.debug("original", 8)
    // grid.debugBig("original", 8)

    val oh = grid.size
    val ow = grid[0].size
    val h = grid.size * 5
    val w = grid[0].size * 5

    val startG = 0f + value2(oh, ow, grid)(0,0)
    val startH = heuristic2()(0, 0, h - 1, w - 1)
    val startF = startG + startH

    val endNode = findPath(
        h,
        w,
        Node(0, 0, null, startF, startG, startH),
        Node(h - 1, w - 1, null, 0f, 0f, 0f),
        value2(oh, ow, grid),
        heuristic2()
    )
    // endNode contains reference to its parent
    val entryCost = grid[0][0]
    val totalCost = calculateCost2(oh, ow, endNode, grid)

    return totalCost - entryCost
}

private fun part1(input: List<String>): Long {
    val grid = parse(input)
    // grid.debug("original", 4)

    val h = grid.size
    val w = grid[0].size

    val endNode = findPath(
        h,
        w,
        Node(0, 0, null, 0f, 0f, 0f),
        Node(h - 1, w - 1, null, 0f, 0f, 0f),
        { y, x -> grid[y][x] }
    ) { y, x, endY, endX ->
        // euclidean squared - (((y - endY).toDouble()).pow(2.0) + ((x - endX).toDouble()).pow(2.0)).toFloat()
        // manhattan distance - abs(endY - y) + abs(endX - x)
        (abs(endY - y) + abs(endX - x)).toFloat()
    }
    // endNode contains reference to its parent
    val entryCost = grid[0][0]
    val totalCost = calculateCost(endNode, grid)

    if (false) {
        debugBacktrackPath(endNode, grid)
        grid.debug("Final Path", 5)
    }

    return totalCost - entryCost
}

data class Node(val y: Int, val x: Int, val parent: Node? = null, val f: Float, val g: Float, val h: Float) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Node

        if (y != other.y) return false
        if (x != other.x) return false

        return true
    }

    override fun hashCode(): Int {
        var result = y
        result = 31 * result + x
        return result
    }

    override fun toString(): String {
        return "Node(y=$y, x=$x, f=$f)"
    }
}

private fun debugBacktrackPath(node: Node, grid: Array<Array<Int>>): List<Pair<Int, Int>> {
    val path = mutableListOf<Pair<Int, Int>>()
    var current: Node? = node
    while (current != null) {
        path.add(Pair(current.y, current.x))
        grid[current.y][current.x] *= -1
        current = current.parent
    }

    path.reverse()
    return path
}

private fun calculateCost2(oh: Int, ow: Int, node: Node, grid: Array<Array<Int>>): Long {
    var totalCost: Long = 0
    var current: Node? = node
    while (current != null) {
        val y = current.y
        val x = current.x

        val multY = (y / oh)
        val my = y % oh
        val multX = (x / ow)
        val mx = x % ow

        var value = grid[my][mx] + multX + multY
        if (value != 9) {
            value %= 9
        }

        if (value == 0) value = 1

        totalCost += value
        current = current.parent
    }

    return totalCost
}


private fun calculateCost(node: Node, grid: Array<Array<Int>>): Long {
    var totalCost: Long = 0
    var current: Node? = node
    while (current != null) {
        totalCost += grid[current.y][current.x]
        current = current.parent
    }

    return totalCost
}

private fun findPath(
    gridH: Int,
    gridW: Int,
    start: Node,
    end: Node,
    value: (y: Int, x: Int) -> Int,
    heuristic: (y: Int, x: Int, endY: Int, endX: Int) -> Float
): Node {

    val compareByCost: Comparator<Node> = compareBy { it.f }
    val openList = PriorityQueue(compareByCost)
    val closedList = mutableSetOf<Pair<Int, Int>>()

    // Down, Left, Right
    val movements = mutableListOf(Pair(1, 0), Pair(-1, 0), Pair(0, -1), Pair(0, 1))
    val possibleChildren = mutableListOf<Node>()

    openList.add(start)
    while (openList.isNotEmpty()) {
        val currentNode = openList.remove()
        closedList.add(Pair(currentNode.y, currentNode.x))

        if (currentNode == end) {
            println("Found end | totalCost: $currentNode")
            return currentNode
        }

        // Create possible children from the current position
        possibleChildren.clear()
        for ((dy, dx) in movements) {
            val y = currentNode.y + dy
            val x = currentNode.x + dx

            // Exclude impossible positions
            if (y < 0 || y >= gridH) continue
            if (x < 0 || x >= gridW) continue

            // Exclude already visited
            if (closedList.contains(Pair(y, x))) continue

            // G -> distance between node and start node
            // In this case, we're less interested in the distance but the accumulated risk level.
            // So we start accumulating the risk,starting with the risk of the start position and
            // incrementing the risk of the children next to it.
            // H -> heuristic - usually distance between node and end node, usually euclidean distance.
            // F -> G + H
            val g = currentNode.g + value(y, x)
            val h = heuristic(y, x, end.y, end.x)

            possibleChildren.add(
                Node(
                    parent = currentNode,
                    y = y,
                    x = x,
                    g = g,
                    h = h,
                    f = g + h
                )
            )
        }

        for (child in possibleChildren) {
            // Exclude if the node already is set to be visited and is farther away.
            val alreadyExists = openList.find { it.y == child.y && it.x == child.x && it.f < child.f }
            if (alreadyExists != null) {
                continue
            }

            openList.add(child)
        }

    }

    throw RuntimeException("Couldn't find the end")
}

private fun parse(input: List<String>): Array<Array<Int>> {
    val h = input.size
    val w = input[0].length
    val grid = Array(h) { hIdx ->
        val line = input[hIdx].split("").filter { it.isNotEmpty() }.map { it.toInt() }
        Array(w) { wIdx -> line[wIdx] }
    }

    return grid
}

private fun <T> Array<Array<T>>.debug(message: String, pad: Int = 0) {
    val h = this.size
    val w = this[0].size
    println("--$message--")
    for (y in 0 until h) {
        for (x in 0 until w) {
            print("${this[y][x]}".padStart(pad))
        }
        println()
    }
    println()
}
