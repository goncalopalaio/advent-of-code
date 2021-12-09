import java.io.File

private fun main() = day8()

fun day8() {
    val day = "day_8"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val inputDemo1 = File("inputs/2021/${day}_demo_small.txt").readLines()
    val inputDemo2 = File("inputs/2021/${day}_demo_small_2.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 26) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo 1)", expected = 5353) { part2(inputDemo1) }
    runProblem("Part 2 (Demo 2)") { part2(inputDemo2) }
    runProblem("Part 2 (Demo)") { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Long {
    val lines = parse2(input)

    /**
     *  aaaa
     * b    c
     * b    c
     *  dddd
     * e    f
     * e    f
     *  gggg
     */

    var totalSum: Long = 0
    var lineIdx = -1
    for (line in lines) {
        lineIdx++
        val inputs = line.first

        // 0 - 6 segments
        // 1 - 2 segments
        // 2 - 5 segments
        // 3 - 5 segments
        // 4 - 4 segments
        // 5 - 5 segments
        // 6 - 6 segments
        // 7 - 3 segments
        // 8 - 7 segments
        // 9 - 6 segments

        val length6Signals = inputs.filter { it.size == 6 }
        val length5Signals = inputs.filter { it.size == 5 }

        val oneSignal = inputs.find { it.size == 2 }!!
        val fourSignal = inputs.find { it.size == 4 }!!
        val sevenSignal = inputs.find { it.size == 3 }!!
        val eightSignal = inputs.find { it.size == 7 }!!

        val uniqueFromFour = fourSignal.filter { !oneSignal.contains(it) }
        val uniqueFromSeven = sevenSignal.filter { !oneSignal.contains(it) }[0]


        val nineSignal = length6Signals.find {
            it.contains(uniqueFromSeven)
                    && it.contains(fourSignal[0])
                    && it.contains(fourSignal[1])
                    && it.contains(fourSignal[2])
                    && it.contains(fourSignal[3])
        }!!

        // 0 - 6 segments
        // 6 - 6 segments
        // 9 - 6 segments
        val sixSignal = length6Signals.find {
            !(it.contains(oneSignal[0]) && it.contains(oneSignal[1]))
        }!!

        // 2 - 5 segments
        // 3 - 5 segments
        // 5 - 5 segments

        val threeSignal = length5Signals.find {
            it.contains(oneSignal[0]) && it.contains(oneSignal[1])
        }!!

        // 0 - 6 segments
        // 6 - 6 segments
        // 9 - 6 segments
        val zeroSignal = length6Signals.find {
            !(it.contains(uniqueFromFour[0]) && it.contains(uniqueFromFour[1]))
        }!!

        // 2 - 5 segments
        // 3 - 5 segments
        // 5 - 5 segments
        val fiveSignal = length5Signals.find {
            !it.contentEquals(threeSignal) && it.contains(uniqueFromFour[0]) && it.contains(uniqueFromFour[1])
        }!!
        val twoSignal = length5Signals.find { !it.contentEquals(threeSignal) && !it.contentEquals(fiveSignal) }!!

        var number = ""
        val outputs = line.second
        for (output in outputs) {
            number += intoNumber(
                zeroSignal,
                oneSignal,
                twoSignal,
                threeSignal,
                fourSignal,
                fiveSignal,
                sixSignal,
                sevenSignal,
                eightSignal,
                nineSignal,
                output
            )
        }

        totalSum += number.toInt()
    }

    return totalSum
}

private fun intoNumber(
    zeroSignal: Array<Char>,
    oneSignal: Array<Char>,
    twoSignal: Array<Char>,
    threeSignal: Array<Char>,
    fourSignal: Array<Char>,
    fiveSignal: Array<Char>,
    sixSignal: Array<Char>,
    sevenSignal: Array<Char>,
    eightSignal: Array<Char>,
    nineSignal: Array<Char>,
    output: Array<Char>
): Int {

    if (zeroSignal.contentEquals(output)) return 0
    if (oneSignal.contentEquals(output)) return 1
    if (twoSignal.contentEquals(output)) return 2
    if (threeSignal.contentEquals(output)) return 3
    if (fourSignal.contentEquals(output)) return 4
    if (fiveSignal.contentEquals(output)) return 5
    if (sixSignal.contentEquals(output)) return 6
    if (sevenSignal.contentEquals(output)) return 7
    if (eightSignal.contentEquals(output)) return 8
    if (nineSignal.contentEquals(output)) return 9

    println("${"zero".padEnd(11)} = ${zeroSignal.joinToString()}")
    println("${"one".padEnd(11)} = ${oneSignal.joinToString()}")
    println("${"two".padEnd(11)} = ${twoSignal.joinToString()}")
    println("${"three".padEnd(11)} = ${threeSignal.joinToString()}")
    println("${"four".padEnd(11)} = ${fourSignal.joinToString()}")
    println("${"five".padEnd(11)} = ${fiveSignal.joinToString()}")
    println("${"six".padEnd(11)} = ${sixSignal.joinToString()}")
    println("${"seven".padEnd(11)} = ${sevenSignal.joinToString()}")
    println("${"eight".padEnd(11)} = ${eightSignal.joinToString()}")
    println("${"nine".padEnd(11)} = ${nineSignal.joinToString()}")

    throw RuntimeException("Mapping failed. output: ${output.joinToString()}")
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

private fun parse2(input: List<String>): List<Pair<List<Array<Char>>, List<Array<Char>>>> {
    val parsed = mutableListOf<Pair<List<Array<Char>>, List<Array<Char>>>>()
    for (line in input) {
        val sections = line.split("|")

        val outputs = sections[0].split(" ").filter { it.isNotBlank() }.map {
            it.trim().toCharArray().sortedArray().toTypedArray()
        }

        val orderedBySize = outputs.sortedBy { it.size }

        val results = sections[1].split(" ").filter { it.isNotBlank() }.map {
            it.trim().toCharArray().sortedArray().toTypedArray()
        }

        parsed.add(Pair(orderedBySize, results))
    }
    return parsed
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
