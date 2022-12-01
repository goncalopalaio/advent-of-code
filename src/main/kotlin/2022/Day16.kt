import java.io.File
import javax.swing.text.Position
import kotlin.math.pow

private fun main() = day16()

fun day16() {
    val day = "day_16"
    val inputDemo1 = File("inputs/2021/${day}_demo1.txt").readLines()
    val inputDemo2 = File("inputs/2021/${day}_demo2.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)") { part1(inputDemo1, inputDemo2) }
    // runProblem("Part 1") { part1(input) }
    // runProblem("Part 2 (Demo)") { part2(inputDemo) }
    // runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Int {
    return 0
}

sealed class Packet
data class LiteralValue(val literal: Int) : Packet()

private fun parseLiteralValue(packet: IntArray, start: Int, result: MutableList<Packet>): Int {
    var endingPosition = start
    val group = mutableListOf<Int>()
    var s = 0
    while (true) {
        group.add(packet[start + s + 1])
        group.add(packet[start + s + 2])
        group.add(packet[start + s + 3])
        group.add(packet[start + s + 4])

        if (packet[start + s] == 0) break
        s += 5
        endingPosition += 5
    }

    val number = group.toIntArray().toInt(0, group.size - 1)

    result.add(LiteralValue(number))
    return endingPosition + 1
}

private fun parseOperatorUsingLengthInBits(packet: IntArray, start: Int, result: MutableList<Packet>): Int {
    val subPacketTotalLengthInBits = packet.toInt(0, 15, start = start)
    parsePacket(packet, start + 15, result, limit = subPacketTotalLengthInBits)
    return start + subPacketTotalLengthInBits
}

private fun parsePacket(packet: IntArray, initialPosition: Int, result: MutableList<Packet>, limit: Int?): Int {
    var position = initialPosition
    while (true) {
        if (limit != null && position >= limit) break
        if ((position + 6) >= packet.size) break

        val packetVersion = packet.toInt(0, 2, start = position)
        val typeId = packet.toInt(3, 5, start = position)

        position += 6
        if (typeId == 4) {
            position = parseLiteralValue(packet, position, result)
        } else if (typeId == 6) {
            val lengthTypeId = packet[position]
            val totalLengthMode = lengthTypeId == 0

            if (totalLengthMode) {
               parseOperatorUsingLengthInBits(packet, position + 1, result)
            } else {
                //val subPacketTotalPackets = packet.toInt(7, 7 + 11, start = start)
                //println("$packetVersion|$typeId|")
                println("!!! ") // subPacketTotalPackets=$subPacketTotalPackets™
            }
        } else {
            continue
        }

    }

    return position + 1
}

private fun part1(inputDemo1: List<String>, inputDemo2: List<String>): Int {
    val result1 = mutableListOf<Packet>()
    val result2 = mutableListOf<Packet>()
    if (false) {
        generalTests()
        val packet1 = parse(inputDemo1)
        val labels1 = CharArray(packet1.size) { '_' }
        val a1 = parsePacket(packet1, 0, result1, null)
        println(packet1.joinToString(separator = ""))
        println(labels1.joinToString(separator = ""))
    }
    val packet2 = parse(inputDemo2)
    val labels2 = CharArray(packet2.size) { '-' }
    val a2 = parsePacket(packet2, 0, result2, null)
    println("$inputDemo2")
    println("$a2 | $result2")
    println(packet2.joinToString(separator = ""))

    return 0
}

private fun IntArray.toInt(begin: Int, end: Int, start: Int = 0): Int {
    var value = 0
    for ((n, i) in ((end + start) downTo (begin + start)).withIndex()) {
        value += this[i] * 2f.pow(n).toInt()
    }
    return value
}

private fun parse(input: List<String>): IntArray {
    println(input)
    val letters = input[0].split("").toList().flatMap { it.toCharArray().toList() }.map {
        when (it) {
            '0' -> arrayOf(0, 0, 0, 0)
            '1' -> arrayOf(0, 0, 0, 1)
            '2' -> arrayOf(0, 0, 1, 0)
            '3' -> arrayOf(0, 0, 1, 1)
            '4' -> arrayOf(0, 1, 0, 0)
            '5' -> arrayOf(0, 1, 0, 1)
            '6' -> arrayOf(0, 1, 1, 0)
            '7' -> arrayOf(0, 1, 1, 1)
            '8' -> arrayOf(1, 0, 0, 0)
            '9' -> arrayOf(1, 0, 0, 1)
            'A' -> arrayOf(1, 0, 1, 0)
            'B' -> arrayOf(1, 0, 1, 1)
            'C' -> arrayOf(1, 1, 0, 0)
            'D' -> arrayOf(1, 1, 0, 1)
            'E' -> arrayOf(1, 1, 1, 0)
            'F' -> arrayOf(1, 1, 1, 1)
            else -> throw RuntimeException("Unexpected value $it")
        }
    }.flatMap { it.toList() }

    return IntArray(letters.size) { letters[it] }
}

private fun generalTests() {
    assertEquals(0, intArrayOf(0, 0, 0, 0).toInt(0, 3))
    assertEquals(1, intArrayOf(0, 0, 0, 1).toInt(0, 3))
    assertEquals(2, intArrayOf(0, 0, 1, 0).toInt(0, 3))
    assertEquals(3, intArrayOf(0, 0, 1, 1).toInt(0, 3))
    assertEquals(4, intArrayOf(0, 1, 0, 0).toInt(0, 3))
    assertEquals(5, intArrayOf(0, 1, 0, 1).toInt(0, 3))
    assertEquals(6, intArrayOf(0, 1, 1, 0).toInt(0, 3))
    assertEquals(7, intArrayOf(0, 1, 1, 1).toInt(0, 3))
    assertEquals(8, intArrayOf(1, 0, 0, 0).toInt(0, 3))
    assertEquals(9, intArrayOf(1, 0, 0, 1).toInt(0, 3))
    assertEquals(10, intArrayOf(1, 0, 1, 0).toInt(0, 3))
    assertEquals(11, intArrayOf(1, 0, 1, 1).toInt(0, 3))
    assertEquals(12, intArrayOf(1, 1, 0, 0).toInt(0, 3))
    assertEquals(13, intArrayOf(1, 1, 0, 1).toInt(0, 3))
    assertEquals(14, intArrayOf(1, 1, 1, 0).toInt(0, 3))
    assertEquals(15, intArrayOf(1, 1, 1, 1).toInt(0, 3))

    assertEquals(6, intArrayOf(1, 1, 0).toInt(0, 2))
    assertEquals(4, intArrayOf(1, 0, 0).toInt(0, 2))
    assertEquals(2021, intArrayOf(0, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1).toInt(0, 11))
}

private fun <T> assertEquals(expected: T, actual: T) {
    if (expected != actual) throw RuntimeException("Not equal | expected=$expected actual=$actual")
}