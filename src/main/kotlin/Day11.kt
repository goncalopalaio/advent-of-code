import harness.runProblem
import java.io.File
import kotlin.math.abs

private fun main() = day11()

fun day11() {
    val day = "day_11"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 10605L) { result(part1(inputDemo, 20)) }
    runProblem("Part 1", expected = 66802L) { result(part1(input, 20)) }
    runProblem("Part 2 (Demo)", expected = 2713310158L) { result(part2(inputDemo, 10_000)) }
    runProblem("Part 2") { result(part2(input, 10_000)) }
}

/**
 * Greatest common divisor
 */
private fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return abs(a)
    return gcd(b, a % b)
}

/**
 * Least common multiple
 */
private fun lcm(a: Long, b: Long): Long {
    return (a * b) / gcd(a, b)
}

/**
 * The LCM is commutative:
 * LCM(a, b, c) = LCM(LCM(a, b), c) = LCM(a, LCM(b, c))
 */
private fun lcmList(list: List<Long>): Long {
    var prev = lcm(list[0], list[1])

    for (i in 2 until list.size) {
        prev = lcm(prev, list[i])
    }

    return prev
}

private fun part2(input: List<String>, rounds: Int): List<Monkey> {
    val monkeys = parse(input)

    val testValues = monkeys.map { it.test }
    val commonMultiple = lcmList(testValues)

    // println("testValues=$testValues")
    // println("commonMultiple=$commonMultiple")

    return simulate(monkeys, rounds, commonMultiple) {
        it
    }
}

private fun part1(input: List<String>, rounds: Int): List<Monkey> {
    val monkeys = parse(input)

    return simulate(monkeys, rounds, null) {
        it / 3L
    }
}

private fun simulate(
    monkeys: List<Monkey>,
    rounds: Int,
    commonMultiple: Long?,
    strategy: (Long) -> Long
): List<Monkey> {
    for (round in 1.. rounds) {
        for (m in monkeys.indices) {
            val monkey = monkeys[m]
            while (true) {
                var newValue = monkey.startingItems.removeFirstOrNull() ?: break
                monkeys[m].inspections++

                newValue = operation(newValue, monkey.operation[3], monkey.operation[4])
                newValue = strategy(newValue)

                newValue = if (commonMultiple == null) {
                    newValue
                } else {
                    newValue % commonMultiple
                }
                val test = newValue % monkey.test

                // println("Monkey:$m | $previousValue | ${monkey.operation[3]} ${monkey.operation[4]} = $newValue (${test == 0L})")

                val newMonkeyIdx = if (test == 0L) {
                    monkey.testTrue
                } else {
                    monkey.testFalse
                }
                // println("Monkey:$m | Throws to $newMonkeyIdx $newValue")
                monkeys[newMonkeyIdx].startingItems.addLast(newValue)
            }
        }

        // val samplingRounds = setOf(
        //     1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000
        // )

        // if (round in samplingRounds) {
        //     println("round: $round")
        //     result(monkeys)
        // }
    }
    return monkeys
}

private fun result(monkeys: List<Monkey>): Long {
    val sorted = monkeys.sortedBy { it.inspections }
    // for (i in sorted.indices) {
    //     val monkey = sorted[i]
    //     println("${monkey.name} inspected ${monkey.inspections}")
    // }
    // println("Using ${sorted[sorted.lastIndex].inspections} and ${sorted[sorted.lastIndex - 1].inspections}")
    return sorted[sorted.lastIndex].inspections * sorted[sorted.lastIndex - 1].inspections
}

private fun operation(
    current: Long,
    operation: Operation,
    quantity: Operation,
): Long {
    val quantityValue = when (quantity) {
        is Number -> quantity.value
        Old -> current
        else -> error("quantity=$quantity")
    }
    return when (operation) {
        Mul -> (current) * (quantityValue)
        Sum -> (current) + (quantityValue)
        else -> error("$operation")
    }
}

class Monkey(
    val name: Int,
    val startingItems: ArrayDeque<Long>,
    val operation: List<Operation>,
    val test: Long,
    val testTrue: Int,
    val testFalse: Int,
    var inspections: Long = 0L,
)

sealed class Operation
object New : Operation() {
    override fun toString() = "New"
}

object Old : Operation() {
    override fun toString() = "Old"
}

object Sum : Operation() {
    override fun toString() = "Sum"
}

object Mul : Operation() {
    override fun toString() = "Mul"
}

object Div : Operation() {
    override fun toString() = "Div"
}

object Sub : Operation() {
    override fun toString() = "Sub"
}

object Eq : Operation() {
    override fun toString() = "Eq"
}

data class Number(val value: Long) : Operation() {
    override fun toString() = "Number=$value"
}

private fun parse(input: List<String>): List<Monkey> {
    var i = 0
    var name = 0
    val monkeys = ArrayList<Monkey>()
    while (i < input.size) {
        val monkeyNumber = input[i].substringAfter("Monkey ").replace(":", "").toInt()
        val startingItems =
            ArrayDeque(
                input[i + 1].substringAfter("  Starting items: ").split(", ").map { it.toLong() })
        val operation = parseOperation(input[i + 2].substringAfter("  Operation: "))
        val test = parseTest(input[i + 3])
        val testTrue = parseTestOutcomeTrue(input[i + 4])
        val testFalse = parseTestOutcomeFalse(input[i + 5])

        i += 7 // Includes empty line

        // println("monkeyNumber=$monkeyNumber")
        // println("startingItems=$startingItems")
        // println("operation=$operation")
        // println("test=$test")
        // println("testTrue=$testTrue")
        // println("testFalse=$testFalse")

        monkeys.add(Monkey(name, startingItems, operation, test, testTrue, testFalse))
        name++
    }

    return monkeys
}

private fun parseTest(text: String): Long {
    if (text.startsWith("  Test: divisible by ")) {
        return text.substringAfter("  Test: divisible by ").toLong()
    }
    error("Unexpected line | $text")
}

private fun parseTestOutcomeTrue(text: String): Int {
    if (text.startsWith("    If true: throw to monkey ")) {
        return text.substringAfter("    If true: throw to monkey ").toInt()
    }
    error("Unexpected line | $text")
}

private fun parseTestOutcomeFalse(text: String): Int {
    if (text.startsWith("    If false: throw to monkey ")) {
        return text.substringAfter("    If false: throw to monkey ").toInt()
    }
    error("Unexpected line | $text")
}

private fun parseOperation(text: String): List<Operation> {
    val operation = mutableListOf<Operation>()
    val parts = text.split(" ")
    for (p in parts) {
        val op = when (p) {
            "new" -> New
            "=" -> Eq
            "old" -> Old
            "+" -> Sum
            "*" -> Mul
            "/" -> Div
            "-" -> Sub
            else -> {
                val number = p.toLong()
                Number(number)
            }
        }
        operation.add(op)
    }
    return operation
}