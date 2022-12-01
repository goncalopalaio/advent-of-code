import java.io.File

private fun main() = day12()

fun day12() {
    val day = "day_12"
    val inputDemo1 = File("inputs/2021/${day}_demo1.txt").readLines()
    val inputDemo2 = File("inputs/2021/${day}_demo2.txt").readLines()
    val inputDemo3 = File("inputs/2021/${day}_demo3.txt").readLines()
    val input = File("inputs/2021/$day.txt").readLines()

    runProblem("Part 1 (Demo 1)", expected = 10) { part1(inputDemo1) }
    runProblem("Part 1 (Demo 2)", expected = 19) { part1(inputDemo2) }
    runProblem("Part 1 (Demo 3)", expected = 226) { part1(inputDemo3) }
    runProblem("Part 1") { part1(input) }
    runProblem("Part 2 (Demo 1)", expected = 36) { part2(inputDemo1) }
    runProblem("Part 2") { part2(input) }
}

private fun part2(input: List<String>): Long {
    val nodeNames = parseNodeNames(input)
    val connections = parseConnections(nodeNames, input)
    println("nodeNames:$nodeNames")
    println("connections:$connections")

    return traverse2(ArrayList(mutableListOf("start")), connections, null)
}

private fun traverse2(
    path: List<String>,
    connections: HashMap<String, HashSet<String>>,
    repeatedNode: String?
): Long {
    val last = path.last()
    if (last == "end") {
        return 1
    }

    var pathLen = 0L
    for (next in connections[path.last()]!!) {
        if (next == "start") continue
        val nextIsLowerCase = next.lowercase() == next

        if (next == repeatedNode) continue
        if (nextIsLowerCase && repeatedNode == null && path.contains(next)) {
            pathLen += traverse2(path + next, connections, next)
            continue
        }
        if (nextIsLowerCase && path.contains(next)) continue

        pathLen += traverse2(path + next, connections, repeatedNode)
    }

    return pathLen
}

private fun part1(input: List<String>): Long {
    val nodeNames = parseNodeNames(input)
    val connections = parseConnections(nodeNames, input)
    println("nodeNames:$nodeNames")
    println("connections:$connections")

    return traverse1(ArrayList(mutableListOf("start")), connections, 0)
}

private fun traverse1(path: List<String>, connections: HashMap<String, HashSet<String>>, depth: Int): Long {
    debug("$path", depth = depth)

    val last = path.last()
    if (last == "end") {
        debug("ENDING", depth = depth)
        return 1
    }

    var pathLen = 0L
    for (next in connections[path.last()]!!) {
        if (next.lowercase() == next && path.contains(next)) continue
        pathLen += traverse1(path + next, connections, depth + 1)
    }

    return pathLen
}

private fun parseConnections(nodeNames: List<String>, input: List<String>): HashMap<String, HashSet<String>> {
    val connections = hashMapOf<String, HashSet<String>>()
    for (name in nodeNames) {
        connections[name] = HashSet()
    }
    for (line in input) {
        val (src, dst) = line.split("-")
        connections[src]!!.add(dst)
        connections[dst]!!.add(src)
    }

    return connections
}

private fun parseNodeNames(input: List<String>): List<String> {
    val nodeNames = ArrayList<String>()
    for (line in input) {
        val (src, dst) = line.split("-")
        if (!nodeNames.contains(src)) {
            nodeNames.add(src)
        }
        if (!nodeNames.contains(dst)) {
            nodeNames.add(dst)
        }
    }

    return nodeNames
}

private fun debug(message: String, depth: Int = 0) {
    if (depth == 0) {
        println(message)
    } else {
        println("${" ".repeat(depth * 4)}$message")
    }
}