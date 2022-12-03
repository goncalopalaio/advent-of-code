import Outcome.*
import Play.*
import harness.runProblem
import java.io.File

private fun main() = day2()

enum class Play {
    Rock,
    Paper,
    Scissor,
}

enum class Outcome {
    Win,
    Lose,
    Draw,
}

fun day2() {
    val day = "day_2"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 15) { part1(inputDemo) }
    runProblem("Part 1", expected = 9241) { part1(input) }
    runProblem("Part 2 (Demo)", expected = 12) { part2(inputDemo) }
    runProblem("Part 2", expected = 14610) { part2(input) }
}

private fun part2(originalInput: List<String>): Int {
    val input = parse2(originalInput)

    return input.fold(0) { total, (other, outcome) ->
        val play = forceOutcome(other, outcome)
        val playScore = playScore(play)
        val outcomeScore = outcomeScore(outcome(other, play))
        total + playScore + outcomeScore
    }
}

private fun part1(originalInput: List<String>): Int {
    val input = parse1(originalInput)

    return input.fold(0) { total, (other, me) ->
        val playScore = playScore(me)
        val outcomeScore = outcomeScore(outcome(other, me))
        total + playScore + outcomeScore
    }
}

private fun parse1(input: List<String>) = Array(input.size) {
    val line = input[it]
    charToPlay(line[0]) to charToPlay(line[2])
}

private fun parse2(input: List<String>) = Array(input.size) {
    val line = input[it]
    charToPlay(line[0]) to charToOutcome(line[2])
}

private fun playScore(play: Play) = when (play) {
    Rock -> 1
    Paper -> 2
    Scissor -> 3
}

private fun outcomeScore(outcome: Outcome) = when (outcome) {
    Win -> 6
    Lose -> 0
    Draw -> 3
}

private fun forceOutcome(other: Play, expected: Outcome) = when (other) {
    Rock -> when (expected) {
        Win -> Paper
        Lose -> Scissor
        Draw -> Rock
    }
    Paper -> when (expected) {
        Win -> Scissor
        Lose -> Rock
        Draw -> Paper
    }
    Scissor -> when (expected) {
        Win -> Rock
        Lose -> Paper
        Draw -> Scissor
    }
}

private fun outcome(other: Play, me: Play) = when (other) {
    Rock -> when (me) {
        Rock -> Draw
        Paper -> Win
        Scissor -> Lose
    }
    Paper -> when (me) {
        Rock -> Lose
        Paper -> Draw
        Scissor -> Win
    }
    Scissor -> when (me) {
        Rock -> Win
        Paper -> Lose
        Scissor -> Draw
    }
}

private fun charToPlay(character: Char) = when (character) {
    'A' -> Rock
    'B' -> Paper
    'C' -> Scissor
    'X' -> Rock
    'Y' -> Paper
    'Z' -> Scissor
    else -> error("Invalid input | $character")
}

private fun charToOutcome(character: Char) = when (character) {
    'X' -> Lose
    'Y' -> Draw
    'Z' -> Win
    else -> error("Invalid input | $character")
}