import Debug.log
import java.io.File
import java.lang.RuntimeException

fun main() {
    val inputDemo = File("inputs/2021/day_2_demo.txt").readLines()
    val input = File("inputs/2021/day_2.txt").readLines()

    measurePrint("Part 1 Demo") { part1(inputDemo) }
    measurePrint("Part 1") { part1(input) }

    measurePrint("Part 2 Demo") { part2(inputDemo) }
    measurePrint("Part 2") { part2(input) }

    /**
        150              | Part 1 Demo                      | 0ms
        1488669          | Part 1                           | 1ms
        900              | Part 2 Demo                      | 0ms
        1176514794       | Part 2                           | 1ms
     */
}

private fun part2(input: List<String>): Int {
    var horizontalPosition = 0
    var aim = 0
    var depth = 0

    for (line in input) {
        val direction = line[0]
        val amount: Int
        when (direction) {
            'f' -> {
                amount = line.substring("forward ".length).toInt()
                horizontalPosition += amount
                depth += aim * amount
            }
            'd' -> {
                amount = line.substring("down ".length).toInt()
                aim += amount
            }
            'u' -> {
                amount = line.substring("up ".length).toInt()
                aim -= amount
            }
            else -> {
                throw RuntimeException("Unexpected value, line=$line")
            }
        }

        // log("direction=$direction, amount=$amount")
    }

    // log("horizontalPosition=$horizontalPosition, depth=$depth")
    return horizontalPosition * depth
}

private fun part1(input: List<String>): Int {
    var horizontalPosition = 0
    var depth = 0
    for (line in input) {
        val direction = line[0]
        val amount: Int
        when (direction) {
            'f' -> {
                amount = line.substring("forward ".length).toInt()
                horizontalPosition += amount
            }
            'd' -> {
                amount = line.substring("down ".length).toInt()
                depth += amount
            }
            'u' -> {
                amount = line.substring("up ".length).toInt()
                depth -= amount
            }
            else -> {
                throw RuntimeException("Unexpected value, line=$line")
            }
        }

        // log("direction=$direction, amount=$amount")
    }

    // log("horizontalPosition=$horizontalPosition, depth=$depth")
    return horizontalPosition * depth
}