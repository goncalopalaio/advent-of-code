import harness.runProblem
import java.io.File

private fun main() = day4()

fun day4() {
    val day = "day_4"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 2) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)") { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>) = parse1(input).fold(0) { sum, pair ->
    val (s1, e1, s2, e2) = pair
    val first = HashSet<Long>().addRange(s1, e1)
    val second = HashSet<Long>().addRange(s2, e2)

    val partiallyIntersects = if (first.intersect(second).isNotEmpty()) 1 else 0
    sum + partiallyIntersects
}

private fun part1(input: List<String>) = parse1(input).fold(0) { sum, pair ->
    val (s1, e1, s2, e2) = pair
    val first = HashSet<Long>().addRange(s1, e1)
    val second = HashSet<Long>().addRange(s2, e2)

    val fullyMatches = if (first.size < second.size) {
        val intersection = first.intersect(second)
        if (intersection.size == first.size) 1 else 0
    } else {
        val intersection = second.intersect(first)
        if (intersection.size == second.size) 1 else 0
    }

    sum + fullyMatches
}

fun MutableSet<Long>.addRange(start: Long, end: Long): MutableSet<Long> {
    for (elem in start..end) this.add(elem)
    return this
}

data class Quad(val firstStart: Long, val firstEnd: Long, val secondStart: Long, val secondEnd: Long)

private fun parse1(input: List<String>) = input.map {
    val (first, second) = it.split(",")
    val (firstStart, firstEnd) = first.split("-")
    val (secondStart, secondEnd) = second.split("-")
    Quad(firstStart.toLong(), firstEnd.toLong(), secondStart.toLong(), secondEnd.toLong())
}