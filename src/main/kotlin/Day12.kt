import harness.runProblem
import java.io.File
import java.util.Deque
import java.util.PriorityQueue

private fun main() = day12()

fun day12() {
    val day = "day_12"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 31) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 29) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    val (s, e, grid) = parse(input)

    val starts = ArrayList<IVec2>()
    for (y in 0 until grid.height) {
        for (x in 0 until grid.width) {
            if (grid.at(x, y) == 0) {
                starts.add(IVec2(x, y))
            }
        }
    }
    return shortestPath(grid, starts, e)
}

private fun part1(input: List<String>): Int {
    val (s, e, grid) = parse(input)

    return shortestPath(grid, listOf(s), e)
}

private fun shortestPath(grid: Grid, starts: List<IVec2>, end: IVec2): Int {
    val q = PriorityQueue<Step> { o1, o2 ->
        val d1 = o1?.distance ?: 0
        val d2 = o2?.distance ?: 0
        d1 - d2
    }

    val visited = HashSet<IVec2>()
    val offsets = arrayOf(IVec2(-1, 0), IVec2(1, 0), IVec2(0, -1), IVec2(0, 1))

    for (s in starts) {
        q.add(Step(0, s))
    }

    while (q.isNotEmpty()) {
        val curr = q.remove()
        val (steps, pos) = curr

        if (visited.contains(pos)) continue
        visited.add(pos)

        if (pos.x == end.x && pos.y == end.y) {
            return steps
        }

        val neighbors = offsets.mapNotNull {
            val nx = it.x + pos.x
            val ny = it.y + pos.y
            if (!grid.isAccessible(nx, ny)) return@mapNotNull null
            val cv = grid.at(pos.x, pos.y)
            val nv = grid.at(nx, ny)
            if (nv <= (cv + 1)) IVec2(nx, ny) else null
        }

        for (n in neighbors) {
            q.add(Step(steps + 1, n))
        }
    }

    return -1
}

private fun parse(input: List<String>): Triple<IVec2, IVec2, Grid> {
    var startPos: IVec2? = null
    var endPos: IVec2? = null

    val grid = Array(input.size) { rowIdx ->
        val line = input[rowIdx].toCharArray()
        Array(line.size) { colIdx ->
            val c = when (val char = line[colIdx]) {
                'S' -> {
                    startPos = colIdx v2 rowIdx
                    'a'
                }

                'E' -> {
                    endPos = colIdx v2 rowIdx
                    'z'
                }

                else -> char
            }
            c.code - 'a'.code
        }
    }

    // grid.print(padding = 4)
    println("$startPos, $endPos")
    return Triple(startPos!!, endPos!!, grid)
}

private typealias IVec2 = Vec2<Int, Int>

data class Step(val distance: Int, val pos: IVec2) : Comparable<Step> {
    override fun compareTo(other: Step): Int {
        return distance - other.distance
    }

}