package twentytwentyone

import runProblem
import java.io.File

private fun main() = day14()

fun day14() {
    val day = "day_14"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 1588) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }

    runProblem("Part 2 (Demo) #1", expected = 1588) { part2(inputDemo, 10) }
    runProblem("Part 2 (Demo) #2", expected = 2188189693529L) { part2(inputDemo, 40) }
    runProblem("Part 2") { part2(input, 40) }
}

private fun part2(input: List<String>, steps: Int): Long {
    val (template, rules) = parse(input)
    val polymer = mutableMapOf<String, Long>()
    val counts = mutableMapOf<Char, Long>()

    var start = 0
    while (start < (template.length - 1)) {
        val pair = template.substring(start, start + 2)
        polymer.mapIncrement(pair, 1)
        counts.mapIncrement(pair[0], 1)
        counts.mapIncrement(pair[1], 1)
        start++
    }

    println("polymer: $polymer")
    println("counts:  $counts")
    println("-------")

    repeat(steps) {
        println()
        println("step:${it + 1}")
        runStep(polymer, counts, rules)
        println("polymer: $polymer")
        println("counts:  $counts")
    }

    val max = counts.maxOf { it.value }
    val min = counts.minOf { it.value }

    // You could also sum the characters in the pairs (in polymer) and divide them by 2
    // since each time you add a new polymer with an existing count the new character will be repeated
    // in two patterns two times (x the times the pattern exists)

    return max - min
}

private fun runStep(polymer: MutableMap<String, Long>, counts: MutableMap<Char, Long>, rules: HashMap<String, Char>) {
    val newPolymer = mutableMapOf<String, Long>()
    val newCounts = mutableMapOf<Char, Long>()

    for ((part, count) in polymer) {
        val produced = rules[part] ?: continue
        newPolymer.mapIncrement("${part[0]}$produced", count)
        newPolymer.mapIncrement("$produced${part[1]}", count)
        newCounts.mapIncrement(produced, count)
    }

    polymer.clear()
    polymer.putAll(newPolymer)
    for ((k, n) in newCounts) {
        counts.mapIncrement(k, n)
    }
}

private fun part1(input: List<String>): Int {
    val (template, rules) = parse(input)
    println("template:$template, rules:$rules")

    val (_, counts) = countsPart1(template, rules, 10)
    val min = counts.minOf { it.value }
    val max = counts.maxOf { it.value }

    println("$counts")
    return max - min
}

private fun countsPart1(
    template: String,
    rules: HashMap<String, Char>,
    @Suppress("SameParameterValue") steps: Int
): Pair<String, Map<Char, Int>> {
    var sequence = template

    for (step in 1..steps) {
        var start = 0
        var offset = 0
        var newSequence = sequence
        while (start < (sequence.length - 1)) {
            val pair = sequence.subSequence(start, start + 2)
            if (rules[pair] != null) {
                val position = offset + start + 1
                val part1 = newSequence.substring(0, position)
                val part2 = newSequence.substring(position)

                newSequence = part1 + rules[pair]!! + part2
                offset++
            }
            start++
        }

        sequence = newSequence
    }

    val chars = sequence.split("").filter { it.isNotEmpty() }.map { it[0] }
    return Pair(sequence, chars.groupingBy { it }.eachCount())
}

private fun parse(input: List<String>): Pair<String, HashMap<String, Char>> {
    val template = input[0].trim()
    val rules = LinkedHashMap<String, Char>()
    for (idx in 2 until input.size) {
        val (first, second) = input[idx].split(" -> ")
        rules[first] = second[0]
    }

    return Pair(template, rules)
}

private fun <T> MutableMap<T, Long>.mapIncrement(k: T, value: Long) {
    if (this[k] == null) {
        this[k] = value
    } else {
        this[k] = this[k]!! + value
    }
}