import java.io.File

private fun main() = day10()

fun day10() {
    val day = "day_10"
    val inputDemo = File("inputs/2021/${day}_demo.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 26397) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 288957) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Long {
    val syntax = parse(input)
    val scoring = hashMapOf(
        ")" to 1L,
        "]" to 2L,
        "}" to 3L,
        ">" to 4L,
    )

    val incompleteStacks = mutableListOf<Stack<String>>()
    for (line in syntax) {
        val s = Stack<String>()
        for (element in line) {
            if (isOpenChar(element)) {
                s.push(element)
            } else {
                val poppedElement = s.pop()
                if (!opens(element, poppedElement)) {
                    s.clear()
                    break
                }
            }
        }
        if (s.isNotEmpty()) {
            incompleteStacks.add(s)
        }
    }

    val scores = mutableListOf<Long>()
    for (s in incompleteStacks) {
        s.reverse()
        s.map {
            when (it) {
                "(" -> ")"
                "[" -> "]"
                "{" -> "}"
                "<" -> ">"
                else -> throw RuntimeException("Unexpected value: $it")
            }
        }
        var totalScore = 0L
        for (char in s.listIterator()) {
            totalScore *= 5
            totalScore += scoring[char]!!
        }
        scores.add(totalScore)
    }

    scores.sort()
    return scores[scores.size / 2]
}

private fun part1(input: List<String>): Int {
    val syntax = parse(input)
    val scoring = hashMapOf(
        ")" to 3,
        "]" to 57,
        "}" to 1197,
        ">" to 25137,
    )

    var score = 0
    for (line in syntax) {
        val s = Stack<String>()
        for (element in line) {
            if (isOpenChar(element)) {
                s.push(element)
            } else {
                val poppedElement = s.pop()
                if (!opens(element, poppedElement)) {
                    score += scoring[element]!!
                    break
                }
            }
        }
    }

    return score
}

fun opens(closing: String, open: String?): Boolean {
    if (open == null) return false

    return when (closing) {
        ")" -> open == "("
        "]" -> open == "["
        "}" -> open == "{"
        ">" -> open == "<"
        else -> throw RuntimeException("Unexpected values: '$closing' '$open'")
    }
}

fun isOpenChar(s: String): Boolean {
    return when (s) {
        ")" -> false
        "]" -> false
        "}" -> false
        ">" -> false
        "(" -> true
        "[" -> true
        "{" -> true
        "<" -> true
        else -> throw RuntimeException("Unexpected value: '$s'")
    }
}

class Stack<T> {
    private var backing = ArrayList<T>()

    fun push(element: T) {
        backing.add(element)
    }

    fun pop(): T? {
        val result = if (backing.isEmpty()) {
            null
        } else {
            val value = backing[backing.lastIndex]
            backing.removeAt(backing.lastIndex)
            value
        }

        return result
    }

    fun isNotEmpty() = backing.isNotEmpty()

    fun clear() = backing.clear()

    fun map(mapping: (T) -> T) {
        backing = backing.map { mapping(it) } as ArrayList<T>
    }

    fun reverse() {
        backing.reverse()
    }

    fun listIterator() = backing.iterator()

    override fun toString(): String {
        return "Stack(${backing.joinToString()})"
    }
}

private fun parse(input: List<String>): Array<Array<String>> {
    val output = ArrayList<Array<String>>()
    for (line in input) {
        output.add(line.split("").filter { it.isNotEmpty() }.toTypedArray())
    }

    return output.toTypedArray()
}