import java.io.File
import java.util.*

private fun main() = day3()

fun day3() {
    val day = "day_3"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 198) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 230) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    val oxygenGeneratorRating = calc(input) { onesSum, zerosSum ->
        if (onesSum == zerosSum) {
            true
        } else onesSum > zerosSum
    }

    val co2ScrubberRating = calc(input) { onesSum, zerosSum ->
        if (onesSum == zerosSum) {
            false
        } else onesSum < zerosSum
    }

    println("oxygenGeneratorRating=$oxygenGeneratorRating")
    println("co2ScrubberRating=$co2ScrubberRating")
    val oxygenGeneratorRatingValue = oxygenGeneratorRating.toBinaryString(input[0].length).toInt(2)
    val co2ScrubberRatingValue = co2ScrubberRating.toBinaryString(input[0].length).toInt(2)
    println("oxygenGeneratorRatingValue=$oxygenGeneratorRatingValue")
    println("co2ScrubberRatingValue=$co2ScrubberRatingValue")

    return oxygenGeneratorRatingValue * co2ScrubberRatingValue
}

private fun calc(input: List<String>, method: (onesSum: Int, zerosSum: Int) -> Boolean): BitSet {
    val width = input[0].length
    val numbers = Array(input.size) { parseBitset(input[it]) }

    var remaining = mutableListOf<Int>()
    remaining.addAll(input.indices)

    var col = 0
    while (true) {
        val onesSum = countOnes(col, remaining, numbers)
        val zerosSum = remaining.size - onesSum
        println("sums: ones=$onesSum, zeros=$zerosSum")
        val mostCommon = method(onesSum, zerosSum)

        remaining = remaining.filter { numbers[it].get(col) == mostCommon }.toMutableList()

        if (true) {
            println("remaining: $remaining")
            remaining.forEach { println(numbers[it].toBinaryString(width)) }
        }

        if (remaining.size == 1) return numbers[remaining[0]]
        col++
    }

}

private fun countOnes(col: Int, indices: List<Int>, numbers: Array<BitSet>): Int {
    var count = 0
    indices.forEach {
        if (numbers[it].get(col)) {
            count += 1
        }
    }
    return count
}

private fun part1(input: List<String>): Int {
    val numbers = Array(input.size) { parseLine(input[it]) }
    val width = numbers[0].size
    // printData(numbers)
    var gamma = ""

    for (col in 0 until width) {
        var ones = 0
        for (row in input.indices) {
            val value = numbers[row][col]
            if (value == 1) {
                ones += 1
            }
        }
        val zeros = input.size - ones
        gamma += if (ones > zeros) {
            "0"
        } else {
            "1"
        }
    }

    val gammaInt = gamma.toInt(2)
    val epsilon = gamma.invertBin()
    val epsilonInt = epsilon.toInt(2)
    println("gamma: $gamma :: gammaInt=$gammaInt :: epsilonInt=$epsilonInt")

    return gammaInt * epsilonInt
}

private fun printData(data: Array<Array<Int>>) {
    for (datum in data) {
        for (line in datum) {
            print("$line")
        }
        println()
    }
}

// TODO Replace with bitwise operation
private fun String.invertBin(): String {
    var result = ""
    val characters = this.chars()
    val zero = '0'.code

    characters.forEach {
        result += if (it == zero) {
            "1"
        } else {
            "0"
        }
    }

    return result
}

private fun parseBitset(line: String): BitSet {
    val one = '1'.code
    val characters = line.chars().toArray()
    val set = BitSet(characters.size)
    characters.forEachIndexed { index, char ->
        if (char == one) {
            set.set(index)
        }
    }

    return set
}

private fun BitSet.toBinaryString(len: Int): String {
    var res = ""
    for (idx in 0 until len) {
        res += if (this.get(idx)) {
            "1"
        } else {
            "0"
        }
    }

    return res
}

private fun parseLine(line: String): Array<Int> {
    val characters = line.chars().toArray()
    val zero = '0'.code
    val one = '1'.code

    return Array(line.length) {
        when (characters[it]) {
            zero -> 0
            one -> 1
            else -> throw RuntimeException("Unexpected value: line=$line")
        }
    }
}