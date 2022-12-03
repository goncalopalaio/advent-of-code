import harness.runProblem
import java.io.File

private fun main() = day0()

fun day0() {
    val day = "day_0"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)") { part1(inputDemo) }
    // runProblem("Part 1") { part1(input) }
    // runProblem("Part 2 (Demo)") { part2(inputDemo) }
    // runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    return 0
}

private fun part1(input: List<String>): Int {
    return 0
}

private fun parse(input: List<String>): List<String> {
    for (line in input) {
        println(line)
    }

    return emptyList()
}