import harness.runProblem
import java.io.File
import java.lang.RuntimeException
import java.lang.StringBuilder

const val VERBOSE = false

private fun main() = day13()

fun day13() {
    val day = "day_13"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 13) { part1(inputDemo) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo)", expected = 140) { part2(inputDemo) }
    runProblem("Part 2") { part2(input) }
}

private fun itemSort(input: List<Token>): List<Token> {
    return input.sortedWith { a, b ->
        when (compare(a, b)) {
            Decision.RightOrder -> -1
            Decision.NotInOrder -> 1
            Decision.Nothing -> 0
        }
    }
}

private fun part2(input: List<String>): Int {
    val list = parsePart2(input)
    val sorted = itemSort(list)

    var dividers = ArrayList<Int>()
    for (i in sorted.indices) {
        val item = sorted[i]
        if (item.isDivider) {
            dividers.add(i + 1)
        }
    }
    return dividers.reduce { acc, i -> acc * i }
}

private fun part1(input: List<String>): Int {
    val pairs = parse(input)

    val inRightOrder = ArrayList<Int>()
    val results = ArrayList<Decision>()
    for (i in pairs.indices) {
        val (first, second) = pairs[i]
        debug("== Pair ${i + 1} ==")
        val r = compare(first, second)
        debug("result=$r")
        results.add(r)

        if (r == Decision.RightOrder) inRightOrder.add(i + 1)
    }

    return inRightOrder.sum()
}

enum class Decision {
    RightOrder,
    NotInOrder,
    Nothing,
}

private fun compare(first: Token, second: Token): Decision {
    fun compareLists(first: Arr, second: Arr): Decision {
        debug("- Compare $first vs $second")
        var i = 0
        var noDecision = true
        while (true) {
            if (i >= first.list.size) {
                if (first.list.size == second.list.size && noDecision) return Decision.Nothing
                debug("- Left side ran out of items, so inputs are in the right order")
                return Decision.RightOrder
            }
            if (i >= second.list.size) {
                if (first.list.size == second.list.size && noDecision) return Decision.Nothing
                debug("- Right side ran out of items, so inputs are not in the right order")
                return Decision.NotInOrder
            }
            noDecision = false
            when (val decision = compare(first.list[i], second.list[i])) {
                Decision.RightOrder -> {
                    return decision
                }

                Decision.NotInOrder -> {
                    return decision
                }

                Decision.Nothing -> {
                    noDecision = true
                    i++
                }
            }
        }

        return Decision.Nothing
    }

    fun comp(first: Token, second: Token): Decision {
        if (first is Arr && second is Arr) {
            return compareLists(first, second)
        } else if (first is Num && second is Num) {
            debug("- Compare $first vs $second")
            if (first.number < second.number) {
                debug("- Left side is smaller, so inputs are in the right order")
                return Decision.RightOrder
            }
            if (first.number > second.number) {
                debug("- Right side is smaller, so inputs are not in the right order")
                return Decision.NotInOrder
            }
            return Decision.Nothing
        } else if (first is Num) {
            debug("- Compare $first vs $second")
            debug("- Mixed types; convert left to [9] and retry comparison")
            return comp(Arr(listOf(first)), second)
        } else if (second is Num) {
            debug("- Compare $first vs $second")
            debug("- Mixed types; convert right to [${second.number}] and retry comparison")
            return comp(first, Arr(listOf(second)))
        }

        return Decision.Nothing
    }

    return comp(first, second)
}

private fun tokenizer(line: String): List<String> {
    val tokens = ArrayList<String>()
    var start = 0
    while (start < line.length) {
        val t = line[start]
        if (t == ',' || t == '[' || t == ']') {
            tokens.add(t.toString())
            start++
        } else {
            var cs = ""
            while (true) {
                val cn = line[start]
                if (!cn.isDigit()) break
                cs += cn.toString()
                start++
            }
            tokens.add(cs)
        }
    }
    return tokens
}

fun List<String>.toStringRepresentation(): String {
    val sb = StringBuilder()
    for (e in this) {
        sb.append(e)
    }
    return sb.toString()
}

sealed class Token(var isDivider: Boolean = false)
class Arr(val list: List<Token>) : Token() {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        val last = this.list.lastIndex
        for (i in this.list.indices) {
            sb.append(this.list[i])
            if (i != last) {
                sb.append(",")
            }
        }
        sb.append("]")
        return sb.toString()
    }
}

class Num(val number: Int) : Token() {
    override fun toString(): String {
        return number.toString()
    }
}

private fun toArrays(tokens: List<String>): Token {
    var idx = 0
    fun parse(): Token {
        val packets = ArrayList<Token>()
        while (idx < tokens.size) {
            val t = tokens[idx]
            if (t == "[") {
                idx++
                packets.add(parse())
            } else if (t == "]") {
                idx++
                return Arr(packets)
            } else {
                val start = idx
                numbers@ for (i in start until tokens.size) {
                    val n = tokens[i]
                    if (n == "[" || n == "]") {
                        break@numbers
                    }
                    packets.add(Num(n.toInt()))
                    idx++
                }
            }
        }
        return Arr(packets)
    }

    return parse()
}

private fun parse(lines: List<String>): List<Pair<Token, Token>> {
    val result = ArrayList<Pair<Token, Token>>()
    for ((a, b, _) in lines.chunked(3)) {
        val first = parseSingle(a)
        val second = parseSingle(b)
        // debug(first)
        // debug(second)
        // debug()

        result.add(first to second)
    }
    return result
}

private fun parsePart2(originalLines: List<String>): List<Token> {
    val lines = originalLines.filter { it.isNotBlank() }.toMutableList()
    val result = ArrayList<Token>()
    for (line in lines) {
        val a = parseSingle(line)
        result.add(a)
    }

    val divider1 = parseSingle("[[2]]")
    val divider2 = parseSingle("[[6]]")
    divider1.isDivider = true
    divider2.isDivider = true

    result.add(divider1)
    result.add(divider2)

    return result
}

private fun parseSingle(line: String): Token {

    val t = tokenizer(line)

    val sr = t.toStringRepresentation()
    if (sr != line) throw RuntimeException("Expected $line -> $sr")

    val tokens = t.filter { it != "," }.drop(1) // Avoid the first Array being nested inside another
    val parsedTokens = toArrays(tokens)
    val ts = parsedTokens.toString()
    if (ts != line) throw RuntimeException("Expected $line -> $ts")

    return parsedTokens
}

private fun debug(message: String) {
    if (VERBOSE) println(message)
}