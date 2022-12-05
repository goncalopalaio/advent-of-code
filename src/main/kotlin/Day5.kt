import harness.runProblem
import java.io.File
import kotlin.math.exp

private fun main() = day5()

fun day5() {
    val day = "day_5"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = "CMZ") { part1(inputDemo) }
    runProblem("Part 1", expected = "TGWSMRBPN") { part1(input) }
    runProblem("Part 2 (Demo)", expected = "MCD") { part2(inputDemo) }
    runProblem("Part 2", expected = "TZLTLWRNF") { part2(input) }
}

private fun part2(input: List<String>): String {
    val (stacks, actions) = parse(input)

    for ((quantity, from, to) in actions) {
        val boxes = ArrayList<Char>()
        for (q in 0 until quantity) {
            val popped = stacks[from - 1].pop() ?: break
            boxes.add(popped)
        }
        boxes.reverse()
        for (b in boxes) {
            stacks[to - 1].push(b)
        }
    }

    val sb = StringBuilder()
    for (s in stacks) {
        val element = s.peek() ?: continue
        sb.append(element)
    }

    return sb.toString()
}


private fun part1(input: List<String>): String {
    val (stacks, actions) = parse(input)

    for ((quantity, from, to) in actions) {
        for (q in 0 until quantity) {
            val popped = stacks[from - 1].pop() ?: break
            stacks[to - 1].push(popped)
        }
    }

    val sb = StringBuilder()
    for (s in stacks) {
        val element = s.peek() ?: continue
        sb.append(element)
    }

    return sb.toString()
}

private fun parse(input: List<String>): Pair<Array<BoxedStack<Char>>, ArrayList<Move>> {
    val stackEnd = input.indexOfFirst { !it.contains("[") }

    val stackNumbers = input[stackEnd].split(" ").filter { it.isNotBlank() }.map { it.toInt() }

    val stacks = Array(stackNumbers.size) { BoxedStack<Char>() }
    for (i in 0 until stackEnd) {
        val line = input[i]
        for ((stackPosition, s) in (1 until line.length step 4).withIndex()) {
            val box = line[s]
            if (box == ' ') continue
            val stackNumber = stackNumbers[stackPosition] - 1
            stacks[stackNumber].add(box)
        }
    }

    val actions = ArrayList<Move>()
    for (i in (stackEnd + 2) until input.size) {
        // Use regex?
        val parts = input[i].split(" ") // No destructuring for 6 parts
        actions.add(Move(parts[1].toInt(), parts[3].toInt(), parts[5].toInt()))
    }

    return Pair(stacks, actions)
}

private class BoxedStack<T> {
    private val backing = ArrayList<T>()

    fun add(element: T) {
        backing.add(element)
    }

    fun push(element: T) {
        backing.add(0, element)
    }

    fun pop(): T? {
        if (backing.isEmpty()) return null
        val element = backing[0]
        backing.removeAt(0)
        return element
    }

    fun peek(): T? {
        if (backing.isEmpty()) return null
        return backing[0]
    }

    override fun toString(): String {
        return "BoxedStack($backing)"
    }
}

data class Move(val quantity: Int, val from: Int, val to: Int)