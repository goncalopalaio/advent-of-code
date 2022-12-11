import harness.runProblem
import java.io.File

private fun main() = day10()

fun day10() {
    val day = "day_10"
    val inputDemo1 = File("inputs/2022/${day}_demo_1.txt").readLines()
    val inputDemo2 = File("inputs/2022/${day}_demo_2.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo 1)") { part1(inputDemo1, mutableListOf(1L, 3L, 4L, 5L)) }

    val cycles = mutableListOf(20L, 60L, 100L, 140L, 180L, 220L)
    runProblem("Part 1 (Demo 2)", expected = 13140L) { part1(inputDemo2, cycles) }
    runProblem("Part 1", expected = 14540L) { part1(input, cycles) }

    val cycles2 = mutableListOf(40L, 80L, 120L, 160L, 200L, 240L)
    runProblem("Part 2 (Demo 2)") { part2(inputDemo2, cycles2) }
    runProblem("Part 2") { part2(input, cycles2) }
}

private fun part2(input: List<String>, cycles: List<Long>): Long {
    val instructions = parse(input)
    val sampling = cycles.toMutableList()

    val line = CharArray(40) { ' ' }

    val result = ArrayList<CharArray>()

    var crtPos = 0
    run(instructions, { tick, register ->
        val idx = if (register - 1 == crtPos) {
            register - 1
        } else if (register + 1 == crtPos) {
            register + 1
        } else if (register == crtPos) {
            register
        } else {
            null
        }
        if (idx == null) {
            line[crtPos] = '.'
        } else {
            line[idx] = '#'
        }

        crtPos = (crtPos + 1) % 40

    }, { tick, _ ->
        val sampleTick = sampling.firstOrNull()
        if (sampleTick == tick) {
            sampling.removeAt(0)
            result.add(line.copyOf())
        }
    })

    for (i in result.indices) {
        val a = result[i].joinToString("").replace(".", " ").replace("#", "█")
        println(a)
    }
    return 0
}

private fun part1(input: List<String>, cycles: List<Long>): Long {
    val instructions = parse(input)
    println("$instructions")

    val sampling = cycles.toMutableList()
    val valueAtTick = ArrayList<Pair<Long, Int>>()

    run(instructions, { tick, register ->
        val sampleTick = sampling.firstOrNull()
        if (sampleTick == tick) {
            valueAtTick.add(tick to register)
            sampling.removeAt(0)
        }
    }, { _, _ -> })

    var signal = 0L
    for (r in valueAtTick) {
        println("$r")
        signal += r.first * r.second
    }

    return signal
}

fun run(
    instructions: List<Instruction>,
    before: (tick: Long, register: Int) -> Unit,
    after: (tick: Long, register: Int) -> Unit
) {

    var registerX = 1
    var currentTick = 1L
    for (inst in instructions) {
        for (t in 0 until inst.ticks) {
            before(currentTick, registerX)

            if (t == (inst.ticks - 1)) {
                when (inst) {
                    is AddX -> {
                        registerX += inst.amount
                    }
                    NoOp -> {
                        // Do nothing
                    }
                }
            }
            after(currentTick, registerX)
            currentTick++
        }
    }
}

sealed class Instruction(open val ticks: Long)

data class AddX(val amount: Int) : Instruction(2)

object NoOp : Instruction(1) {
    override fun toString(): String {
        return "NoOp"
    }
}

private fun parse(input: List<String>): ArrayList<Instruction> {
    val instructions = ArrayList<Instruction>()
    for (line in input) {

        val instruction = if (line.startsWith("noop")) {
            NoOp
        } else if (line.startsWith("addx")) {
            val (_, number) = line.split(" ")
            AddX(number.toInt())
        } else {
            // Nothing
            null
        }
        if (instruction != null) instructions.add(instruction)
    }

    return instructions
}