import java.io.File

private fun main() = day8()

fun day8() {
    val day = "day_8"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 26) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    // runProblem("Part 2 (Demo)") { part2(inputDemo) }
    // runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    return 0
}

private fun part1(input: List<String>): Int {
    val digits = parse1(input)
    var count = 0
    for (d in digits) {
        for (n in d) {
            // 8 ; 1 ; 4 ; 7
            if (n.size == 7 || n.size == 2 || n.size == 4 || n.size == 3) count += 1
        }
    }
    return count
}

private fun parse1(input: List<String>): List<Array<MutableList<Char>>> {
    val result = mutableListOf<Array<MutableList<Char>>>()
    for (line in input) {
        val sections = line.split("|")

        val outputs = sections[1].split(" ").filter { it.isNotBlank() }.map { it.trim() }
        val numbers = Array(4) { mutableListOf<Char>() }
        for (oi in outputs.indices) {
            for (seg in outputs[oi]) {
                numbers[oi].add(seg)
            }
        }
        result.add(numbers)
    }
    return result
}

private fun printSegment(segments: Array<Char?>) {
    if (segments.size != 7) throw RuntimeException("Wrong number of segments")

    print(" ")
    val a = if (segments[0] != null) {
        "a"
    } else {
        "_"
    }
    println(" ${a}${a}${a}${a} ")

    val b = if (segments[1] != null) {
        "b"
    } else {
        "_"
    }
    val c = if (segments[2] != null) {
        "c"
    } else {
        "_"
    }
    println("${b}____${c}")
    println("${b}____${c}")

    val d = if (segments[3] != null) {
        "d"
    } else {
        "_"
    }
    println("${d}${d}${d}${d}")
}