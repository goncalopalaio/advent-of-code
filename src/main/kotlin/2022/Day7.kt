import java.io.File
import kotlin.math.abs

private fun main() = day7()

fun day7() {
    val day = "day_7"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 37) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 168) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part1(input: List<String>) = fuelMeter(input) { position, crab -> abs(position - crab) }
private fun part2(input: List<String>): Int = fuelMeter(input) { position, crab -> nSumNaturalNumbers(abs(position - crab)) }
private fun nSumNaturalNumbers(n: Int) = (n * (n + 1)) / 2

private fun fuelMeter(input: List<String>, cost: (position: Int, crab: Int) -> Int): Int {
    val crabs = parse(input)
    val max = crabs.maxOrNull()!!
    val min = crabs.minOrNull()!!

    var minFuel = Int.MAX_VALUE
    for (position in min..max) {
        val fuel = crabs.fold(0) { total, crab -> total + cost(position, crab)}
        if (fuel < minFuel) minFuel = fuel
    }

    return minFuel
}

private fun parse(input: List<String>): IntArray {
    val crabs = input[0].split(",")
    return IntArray(crabs.size) { crabs[it].toInt() }
}