import Debug.log
import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val input = File("inputs/2021/day_1.txt").readLines()
    println("--- Part 1 --- 1162")
    part1(input)

    println("--- Part 2 --- 1190")
    val timePart2 = measureTimeMillis {
        part2(input.map { it.toInt() })
    }
    println("Finished in ${timePart2}ms")

    val timePart2Windowed = measureTimeMillis {
        part2Windowed(input.map { it.toInt() })
    }
    println("Finished in ${timePart2Windowed}ms")
}

private fun part2Windowed(input: List<Int>) {
    var previousSum = 0
    var count = 0

    if (true) {
        input.windowed(3).forEach {
            val sum = it.sum()
            if (previousSum != 0 && sum > previousSum) {
                count += 1
            }
            previousSum = sum
        }
    } else {
        count =
            input
                .asSequence()
                .windowed(3)
                .map { it.sum() }
                .windowed(2)
                .map { if (it[0] < it[1]) 1 else 0 }
                .sum()
    }

    println("Count $count")
}

private fun part2(input: List<Int>) {
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

        // log("$a1 $b1 $c1 | $a2 $b2 $c2 | $aSum > $bSum -> $isIncrease")
    }

    println("Count $count")
}

private fun part1(input: List<String>) {
    var count = 0

    val time = measureTimeMillis {
        var previous: String? = null
        for (line in input) {
            if (previous == null) {
                count += 1
            } else {
                // log("$line | $previous | ${(line > previous)}")
                if (line > previous) {
                    count += 1
                }
            }

            previous = line
        }
    }

    println("Count $count")
    println("Finished in ${time}ms")
}