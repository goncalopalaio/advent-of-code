import java.io.File

private fun main() = day8()

fun day8() {
    val day = "day_8"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)") { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    // runProblem("Part 2 (Demo)") { part2(inputDemo) }
    // runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    return 0
}

private fun part1(input: List<String>): Int {
    return 0
}