import java.io.File

fun main(args: Array<String>) {
    val input = File("inputs/2021/day_1.txt").readLines()
    println("--- Part 1 --- 1162")
    measurePrint("Part 1") { part1(input) }

    println("--- Part 2 --- 1190")
    val intInput = input.map { it.toInt() }
    measurePrint("Part 2") { part2(intInput) }
    measurePrint("Part 2 Windowed V1") { part2WindowedV1(intInput) }
    measurePrint("Part 2 Windowed V2") { part2WindowedV2(intInput) }

    // Note:
    // Apparently there's a way to reduce the problem:
    //  b + c + d > a + b + c  <=>  d > a
    // you could look backwards 1 for part 1 and 3 for part two.
}

private fun part2WindowedV2(input: List<Int>): Int {
    return input
        .asSequence()
        .windowed(3)
        .map { it.sum() }
        .windowed(2)
        .map { if (it[0] < it[1]) 1 else 0 }
        .sum()
}

private fun part2WindowedV1(input: List<Int>): Int {
    var previousSum = 0
    var count = 0

    input.windowed(3).forEach {
        val sum = it.sum()
        if (previousSum != 0 && sum > previousSum) {
            count += 1
        }
        previousSum = sum
    }

    return count
}

private fun part2(input: List<Int>): Int {
    val size = input.size
    var count = 0
    for (i in 0 until size) {
        val a1 = input.getOrNull(i - 0)
        val b1 = input.getOrNull(i + 1)
        val c1 = input.getOrNull(i + 2)

        val a2 = input.getOrNull(i + 1)
        val b2 = input.getOrNull(i + 2)
        val c2 = input.getOrNull(i + 3)

        if (a2 == null || b2 == null || c2 == null) {
            continue
        }
        if (a1 == null || b1 == null || c1 == null) {
            continue
        }

        val aSum = a1 + b1 + c1
        val bSum = a2 + b2 + c2

        val isIncrease = bSum > aSum
        if (isIncrease) count += 1
    }

    return count
}

private fun part1(input: List<String>): Int {
    var count = 0

    var previous: String? = null
    for (line in input) {
        if (previous == null) {
            count += 1
        } else {
            if (line > previous) {
                count += 1
            }
        }

        previous = line
    }

    return count
}