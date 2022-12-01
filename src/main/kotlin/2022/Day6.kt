import java.io.File

private fun main() = day6()

fun day6() {
    val day = "day_6"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 26) { part1(inputDemo, totalDays = 18) }
    runProblem("Part 1 (Demo)", expected = 5934) { part1(inputDemo, totalDays = 80) }
    runProblem("Part 1", expected = 351092) { part1(input, totalDays = 80) }

    runProblem("Part 2 (Demo)", expected = 26984457539) { part2(inputDemo) }
    runProblem("Part 2", expected = 1595330616005) { part2(input) }
}

private fun part2(input: List<String>): Long = part1(input, 256)

private fun part1(input: List<String>, totalDays: Int): Long {
    val fishes = parseInput(input)
    val scheduled = Array<Long>(totalDays) {0}
    for (fish in fishes) {
        var day = fish
        while (day < totalDays) {
            scheduled[day] += 1L
            day += 7
        }
    }

    var count: Long = fishes.size.toLong()

    for (day in 0 until totalDays) {
        val bornInDay = scheduled[day]
        count += bornInDay

        var next = day + 9
        while (next < totalDays) {
            scheduled[next] += bornInDay
            next += 7
        }
    }
    return count
}

private fun parseInput(input: List<String>): Array<Int> {
    return input[0].split(",").map { it.toInt() }.toTypedArray()
}