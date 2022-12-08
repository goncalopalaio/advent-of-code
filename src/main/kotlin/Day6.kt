import harness.runProblem
import java.io.File

private fun main() = day6()

fun day6() {
    val day = "day_6"
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo 1)", expected = 7) { part1(File("inputs/2022/${day}_demo_1.txt").readLines()) }
    runProblem("Part 1 (Demo 2)", expected = 5) { part1(File("inputs/2022/${day}_demo_2.txt").readLines()) }
    runProblem("Part 1 (Demo 3)", expected = 6) { part1(File("inputs/2022/${day}_demo_3.txt").readLines()) }
    runProblem("Part 1 (Demo 4)", expected = 10) { part1(File("inputs/2022/${day}_demo_4.txt").readLines()) }
    runProblem("Part 1 (Demo 5)", expected = 11) { part1(File("inputs/2022/${day}_demo_5.txt").readLines()) }
    runProblem("Part 1") { part1(input) }


    runProblem("Part 2 (Demo 1)", expected = 19) { part2(File("inputs/2022/${day}_demo_1.txt").readLines()) }
    runProblem("Part 2 (Demo 2)", expected = 23) { part2(File("inputs/2022/${day}_demo_2.txt").readLines()) }
    runProblem("Part 2 (Demo 3)", expected = 23) { part2(File("inputs/2022/${day}_demo_3.txt").readLines()) }
    runProblem("Part 2 (Demo 4)", expected = 29) { part2(File("inputs/2022/${day}_demo_4.txt").readLines()) }
    runProblem("Part 2 (Demo 5)", expected = 26) { part2(File("inputs/2022/${day}_demo_5.txt").readLines()) }

    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>) = searchMarker(input, 14)

private fun part1(input: List<String>) = searchMarker(input, 4)

private fun searchMarker(input: List<String>, sequenceLength: Int): Int {
    val buffer = parse(input)
    val map = HashMap<Char, Int>()
    var i = 0
    while(true) {
        if (map.containsKey(buffer[i])) {
            i = map[buffer[i]]!! + 1
            map.clear()
        } else {
            map[buffer[i]] = i
            i++
            if (map.size == sequenceLength) return i
        }
    }
}

/**
 * Input is single line.
 */
private fun parse(input: List<String>) = input[0].toCharArray()