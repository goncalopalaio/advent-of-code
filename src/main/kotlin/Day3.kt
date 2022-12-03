import java.io.File

private fun main() = day3()

fun day3() {
    val day = "day_3"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 157) { part1(inputDemo) }
    runProblem("Part 1", expected = 7742) { part1(input) }
    runProblem("Part 2 (Demo)", expected = 70) { part2(inputDemo) }
    runProblem("Part 2", expected = 2276) { part2(input) }
}

private fun part2(input: List<String>) = parse2(input).fold(0) { sum, elements ->
    if (elements.size != 3) error("Unexpected size | size=${elements.size}")

    val groupA = elements[0].toSet()
    val groupB = elements[1].toSet()
    val groupC = elements[2].toSet()
    val intersection = groupA.intersect(groupB).intersect(groupC)
    if (intersection.size != 1) error("There should be a single common element between three groups | $groupA, $groupB, $groupC")

    val priority = priority(intersection.first())

    sum + priority
}

private fun part1(input: List<String>) = parse1(input).fold(0) { sum, elements ->
    val repeated = HashSet<Char>()
    val halfPoint = elements.size / 2

    for (i in 0 until halfPoint) repeated.add(elements[i])

    var partialSum = 0
    for (i in halfPoint until elements.size) {
        val char = elements[i]
        if (char in repeated) {
            partialSum += priority(char)
            repeated.remove(char)
        }
    }

    sum + partialSum
}

private const val LOWER_CASE_A = 'a'.code
private const val LOWER_CASE_Z = 'z'.code
private const val UPPER_CASE_A = 'A'.code
private const val UPPER_CASE_Z = 'Z'.code

private fun priority(char: Char) = when (val code = char.code) {
    in LOWER_CASE_A..LOWER_CASE_Z -> code - LOWER_CASE_A + 1
    in UPPER_CASE_A..UPPER_CASE_Z -> code - UPPER_CASE_A + 27
    else -> error("Out of range | char=$char")
}

private fun parse1(input: List<String>) = Array(input.size) { input[it].toCharArray() }
private fun parse2(input: List<String>) = input.map { it.toCharArray() }.chunked(3)