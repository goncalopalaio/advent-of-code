import java.io.File

private const val FORWARD_PREFIX = "forward "
private const val DOWN_PREFIX = "down "
private const val UP_PREFIX = "up "

private const val FORWARD_CHAR = 'f'
private const val DOWN_CHAR = 'd'
private const val UP_CHAR = 'u'

private const val FORWARD_PREFIX_LEN = FORWARD_PREFIX.length
private const val DOWN_PREFIX_LEN = DOWN_PREFIX.length
private const val UP_PREFIX_LEN = UP_PREFIX.length

enum class Orientation {
    Forward,
    Down,
    Up
}

sealed class Direction(val amount: Int) {
    class Forward(amount: Int) : Direction(amount)
    class Down(amount: Int) : Direction(amount)
    class Up(amount: Int) : Direction(amount)
}

private fun main() = day2()

fun day2() {
    val inputDemo = File("inputs/2021/day_2_demo.txt").readLines()
    val input = File("inputs/2021/day_2.txt").readLines()

    runProblem("Part 1 Demo") { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }

    runProblem("Part 2 Demo") { part2Version2(inputDemo) }

    val avgMeasurements = mutableListOf<Pair<String, Double>>()
    with(avgMeasurements) {
        add(runProblem("Part 2 (Version 0)", additionalRuns = 512) { part2Version0(input) })
        add(runProblem("Part 2 (Version 1)", additionalRuns = 512) { part2Version1(input) })
        add(runProblem("Part 2 (Version 3)", additionalRuns = 512) { part2Version3(input) })
        add(runProblem("Part 2 (Version 2)", additionalRuns = 512) { part2Version2(input) })
        add(runProblem("Part 2 (Version 4)", additionalRuns = 512) { part2Version4(input) })
    }

    avgMeasurements.sortBy { it.second }
    avgMeasurements.reverse() // fastest last

    avgMeasurements.forEachIndexed { idx, pair ->
        println("$idx :: ${pair.first} | avg=${pair.second} ms")
    }

    // Part 2 (Version 2) appears to be faster most of the time but the results are not consistent
    // the input seems to be too small, all results are sub-millisecond on average so might not be that accurate
}

private fun part2Version4(input: List<String>): Int {
    var horizontalPosition = 0
    var aim = 0
    var depth = 0

    for (line in input) {

        when (parseOrientation(line)) {
            Orientation.Forward -> {
                val amount = parseForwardLine(line)
                horizontalPosition += amount
                depth += aim * amount
            }
            Orientation.Down -> {
                val amount = parseDownLine(line)
                aim += amount
            }
            Orientation.Up -> {
                val amount = parseUpLine(line)
                aim -= amount
            }
        }
    }

    return horizontalPosition * depth
}

private fun part2Version3(input: List<String>): Int {
    var horizontalPosition = 0
    var aim = 0
    var depth = 0

    val directions = Array(input.size) { parseLine(input[it]) }
    for (direction in directions) {
        when (direction) {
            is Direction.Down -> aim += direction.amount
            is Direction.Forward -> {
                horizontalPosition += direction.amount
                depth += aim * direction.amount
            }
            is Direction.Up -> aim -= direction.amount
        }
    }
    return horizontalPosition * depth
}

private fun part2Version2(input: List<String>): Int {
    var horizontalPosition = 0
    var aim = 0
    var depth = 0

    for (line in input) {
        when (val direction = parseLine(line)) {
            is Direction.Down -> aim += direction.amount
            is Direction.Forward -> {
                horizontalPosition += direction.amount
                depth += aim * direction.amount
            }
            is Direction.Up -> aim -= direction.amount
        }
    }

    return horizontalPosition * depth
}

private fun part2Version1(input: List<String>): Int {
    var horizontalPosition = 0
    var aim = 0
    var depth = 0

    for (line in input) {
        val direction = line[0]
        val amount: Int
        when (direction) {
            FORWARD_CHAR -> {
                amount = line.substring(FORWARD_PREFIX_LEN).toInt()
                horizontalPosition += amount
                depth += aim * amount
            }
            DOWN_CHAR -> {
                amount = line.substring(DOWN_PREFIX_LEN).toInt()
                aim += amount
            }
            UP_CHAR -> {
                amount = line.substring(UP_PREFIX_LEN).toInt()
                aim -= amount
            }
            else -> {
                throw RuntimeException("Unexpected value, line=$line")
            }
        }
    }

    return horizontalPosition * depth
}

private fun part2Version0(input: List<String>): Int {
    var horizontalPosition = 0
    var aim = 0
    var depth = 0

    val directions = Array(input.size) { input[it][0] }
    val amounts = Array(input.size) {
        when (directions[it]) {
            FORWARD_CHAR -> {
                input[it].substring(FORWARD_PREFIX_LEN).toInt()
            }
            DOWN_CHAR -> {
                input[it].substring(DOWN_PREFIX_LEN).toInt()
            }
            UP_CHAR -> {
                input[it].substring(UP_PREFIX_LEN).toInt()
            }
            else -> {
                throw RuntimeException("Unexpected value, line=${input[it]}")
            }
        }
    }

    for (idx in input.indices) {
        val direction = directions[idx]
        val amount = amounts[idx]
        when (direction) {
            FORWARD_CHAR -> {
                horizontalPosition += amount
                depth += aim * amount
            }
            DOWN_CHAR -> {
                aim += amount
            }
            UP_CHAR -> {
                aim -= amount
            }
            else -> {
                throw RuntimeException("Unexpected value, direction=$direction, amount=$amount")
            }
        }
    }

    return horizontalPosition * depth
}

private fun part1(input: List<String>): Int {
    var horizontalPosition = 0
    var depth = 0
    for (line in input) {
        when (val direction = parseLine(line)) {
            is Direction.Down -> depth += direction.amount
            is Direction.Forward -> horizontalPosition += direction.amount
            is Direction.Up -> depth -= direction.amount
        }
    }

    return horizontalPosition * depth
}

private fun parseLine(line: String): Direction {
    return when (line[0]) {
        FORWARD_CHAR -> {
            val amount = line.substring(FORWARD_PREFIX.length).toInt()
            Direction.Forward(amount)
        }
        DOWN_CHAR -> {
            val amount = line.substring(DOWN_PREFIX.length).toInt()
            Direction.Down(amount)
        }
        UP_CHAR -> {
            val amount = line.substring(UP_PREFIX.length).toInt()
            Direction.Up(amount)
        }
        else -> {
            throw RuntimeException("Unexpected value, line=$line")
        }
    }
}

private fun parseOrientation(line: String): Orientation {
    return when (line[0]) {
        FORWARD_CHAR -> Orientation.Forward
        DOWN_CHAR -> Orientation.Down
        UP_CHAR -> Orientation.Up
        else -> throw RuntimeException("Unexpected value, line=$line")
    }
}

private fun parseForwardLine(line: String): Int = line.substring(FORWARD_PREFIX.length).toInt()
private fun parseDownLine(line: String): Int = line.substring(DOWN_PREFIX.length).toInt()
private fun parseUpLine(line: String): Int = line.substring(UP_PREFIX.length).toInt()
