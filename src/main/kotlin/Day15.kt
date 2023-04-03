import harness.runProblem
import java.io.File
import java.lang.Math.*
import kotlin.math.absoluteValue

private fun main() = day15()

fun day15() {
    val day = "day_15"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 26) { part1(inputDemo, 10) }
    runProblem("Part 1") { part1(input, 2000000) }
    // runProblem("Part 2 (Demo)") { part2(inputDemo) }
    // runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    return 0
}

private fun part1(input: List<String>, searchLineY: Long): Int {
    val (lines, minP, maxP) = parse(input)
    println("minP:$minP, maxP:$maxP")

    // Given example
    // val distance = P2(8, 7).manhattanDistance(P2(2, 10))
    // assert(distance == 9)

    val ranges = mutableListOf<LongRange>()
    val notABeacon = HashSet<Long>()
    for (a in lines) {
        val range = coveredRanges(a.sensor, a.closestBeacon, searchLineY) ?: continue
        ranges.add(range)

        for (r in range) {
            notABeacon.add(r)
        }
    }

    for (r in ranges) {
        println("$r")
    }

    println("notABeacon=${notABeacon.size}")
    var count = notABeacon.size

    val beacons = lines.map { it.closestBeacon }.toSet()
    for (b in beacons) {
        if (b.y != searchLineY) continue
        println("Removing Beacon $b")
        count--
    }

    return count
}

fun coveredRanges(sensor: P2, beacon: P2, y: Long): LongRange? {
    val distance = sensor.manhattanDistance(beacon)
    // Range decreases one by one while we get to further lines.
    // This will be the range at that specific line.
    val offset = distance - (sensor.y - y).absoluteValue
    return if (offset < 0) {
        null
    } else {
        (sensor.x - offset)..sensor.x + offset
    }
}

data class P2(val x: Long, val y: Long)
data class Line(val sensor: P2, val closestBeacon: P2)

fun P2.manhattanDistance(p2: P2) = kotlin.math.abs(x - p2.x) + kotlin.math.abs(y - p2.y)

data class Input(val lines: List<Line>, val minP: P2, val maxP: P2)

private fun parse(input: List<String>): Input {
    val regex = "Sensor at x=([-0-9]+), y=([-0-9]+): closest beacon is at x=([-0-9]+), y=([-0-9]+)".toRegex()

    val allX = HashSet<Long>()
    val allY = HashSet<Long>()

    val result = ArrayList<Line>()
    for (line in input) {
        val matches = regex.find(line)!!
        val (x1, y1, x2, y2) = matches.destructured

        val closestBeacon = P2(x2.toLong(), y2.toLong())

        result.add(Line(P2(x1.toLong(), y1.toLong()), closestBeacon))

        allX.add(x1.toLong())
        allX.add(x2.toLong())
        allY.add(y1.toLong())
        allY.add(y2.toLong())
    }
    val minX = allX.minOf { it }
    val minY = allY.minOf { it }
    val maxX = allX.maxOf { it }
    val maxY = allY.maxOf { it }
    return Input(result, P2(minX, minY), P2(maxX, maxY))
}