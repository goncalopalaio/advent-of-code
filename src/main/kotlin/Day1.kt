import java.io.File

private fun main() = day1()

fun day1() {
    val day = "day_1"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 24000L) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)") { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Long {
    val map = HashMap<Long, Long>()

    var elf = 0L
    for (line in input) {
        if (line == "") {
            elf++
            continue
        }

        val newValue = line.toLong()

        val update = if (!map.containsKey(elf)) {
            map[elf] = newValue
            newValue
        } else {
            val v = map[elf]!! + newValue
            map[elf] = v
            v
        }
    }

    val elvesByCalories = map.toList().sortedBy { it.second }.reversed()
    var total = 0L
    for (i in 0 until 3) {
        val (_, calories) = elvesByCalories.getOrNull(i) ?: continue
        total += calories
    }

    return total
}

private fun part1(input: List<String>): Long {
    val map = HashMap<Long, Long>()

    var elf = 0L
    var maxElfValue = 0L
    for (line in input) {
        if (line == "") {
            elf++
            continue
        }

        val newValue = line.toLong()

        val update = if (!map.containsKey(elf)) {
            map[elf] = newValue
            newValue
        } else {
            val v = map[elf]!! + newValue
            map[elf] = v
            v
        }

        if (update > maxElfValue) {
            maxElfValue = update
        }
    }

    return maxElfValue
}
