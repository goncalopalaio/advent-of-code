import Debug.log
import java.io.File
import java.lang.RuntimeException

fun main(args: Array<String>) {
    val inputDemo = File("inputs/2021/day_2_demo.txt").readLines()
    measurePrint("Part 1 Demo") { part1(inputDemo) }

    val input = File("inputs/2021/day_2.txt").readLines()
    measurePrint("Part 1") { part1(input) }
}

private fun part1(input: List<String>): Int {
    var horizontalPosition = 0
    var depth = 0
    for (line in input) {
        val direction = line[0]
        val amount: Int
        if (direction == 'f') {
            amount = line.substring("forward ".length).toInt()
            horizontalPosition += amount
        } else if(direction == 'd') {
            amount = line.substring("down ".length).toInt()
            depth += amount
        } else if(direction == 'u') {
            amount = line.substring("up ".length).toInt()
            depth -= amount
        } else {
            throw RuntimeException("Unexpected value, line=$line")
        }

        // log("direction=$direction, amount=$amount")
    }

    // log("horizontalPosition=$horizontalPosition, depth=$depth")
    return horizontalPosition * depth
}