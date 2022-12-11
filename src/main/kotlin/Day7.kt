import harness.runProblem
import java.io.File

private fun main() = day7()

fun day7() {
    val day = "day_7"
    val inputDemo = File("inputs/2022/${day}_demo.txt").readLines()
    val input = File("inputs/2022/$day.txt").readLines()

    runProblem("Part 1 (Demo)", expected = 95437L) { part1(inputDemo) }
    runProblem("Part 1", expected = 1648397L) { part1(input) }
    runProblem("Part 2 (Demo)", expected = 24933642L) { part2(inputDemo) }
    runProblem("Part 2", expected = 1815525L) { part2(input) }
}

private fun part2(input: List<String>): Any {
    val t = parse(input)

    val totalDiskSpace      = 70000000L
    val requiredUnusedSpace = 30000000L
    val usedSpace = t.size(t.root)

    val freeSpace = totalDiskSpace - usedSpace
    if (freeSpace >= requiredUnusedSpace) return 0L

    val minSizeForFolder = requiredUnusedSpace - freeSpace

    val collection = t.min(minSizeForFolder).sortedBy { it.size }
    return collection.first().size
}

private fun part1(input: List<String>): Long {
    val t = parse(input)
    return t.max(100000L).sumOf { it.size }
}

sealed class Node
class MyFile(val name: String, val parent: MyFolder?, val size: Long) : Node()
class MyFolder(val name: String, val parent: MyFolder?, val list: ArrayList<Node>) : Node()

class SizedFolder(val folder: MyFolder, val size: Long)

class Terminal {
    private lateinit var cwd: MyFolder
    lateinit var root: MyFolder

    fun cd() {
        cwd = root
    }

    fun size(folder: MyFolder): Long {
        val content = folder.list
        if (content.isEmpty()) return 0
        val sum = content.sumOf {
            when (it) {
                is MyFile -> it.size
                is MyFolder -> size(it)
            }
        }
        return sum
    }

    fun min(value: Long): List<SizedFolder> {
        val list = ArrayList<SizedFolder>()

        traverseFolders(root) { folder, size ->
            if (size >= value) list.add(SizedFolder(folder, size))
        }

        return list
    }

    fun max(value: Long): List<SizedFolder> {
        val list = ArrayList<SizedFolder>()

        traverseFolders(root) { folder, size ->
            if (size < value) list.add(SizedFolder(folder, size))
        }

        return list
    }

    private fun traverseFolders(folder: MyFolder, visitor: (MyFolder, Long) -> Any): Long {
        val content = folder.list
        if (content.isEmpty()) return 0L
        val sum = content.sumOf {
            when (it) {
                is MyFile -> it.size
                is MyFolder -> traverseFolders(it, visitor)
            }
        }

        visitor(folder, sum)
        return sum
    }

    fun addDirectory(arg: String) {
        if (arg == "..") {
            cwd = cwd.parent!!
        } else {
            if (arg == "/") {
                if (!::root.isInitialized) {
                    root = MyFolder("/", null, ArrayList())
                }
                cwd = root
            } else {
                val folders = cwd.list
                val newFolder = folders.find { it is MyFolder && it.name == arg } as MyFolder
                cwd = newFolder
            }
        }
    }

    fun addFolderToCwd(name: String) {
        cwd.list.add(MyFolder(name, cwd, ArrayList()))
    }

    fun addFileToCwd(name: String, size: Long) {
        cwd.list.add(MyFile(name, cwd, size))
    }

    @Suppress("unused")
    fun cd(name: String) {
        cwd = cwd.list.find { it is MyFolder && it.name == name } as MyFolder
    }

    @Suppress("unused")
    fun ls(folder: MyFolder, level: Int = 0) {
        println("${"\t".repeat(level)} folder=${folder.name} n=${folder.list.size}")
        folder.list.sortBy { it is MyFolder }
        val content = folder.list
        if (content.isEmpty()) return
        for (c in content) {
            when (c) {
                is MyFile -> {
                    println("${"\t".repeat(level + 1)} ${c.name.padEnd(8)} ${c.size}")
                }
                is MyFolder -> {
                    ls(c, level + 1)
                }
            }
        }
    }
}

private fun parse(input: List<String>): Terminal {
    val t = Terminal()

    for (line in input) {
        if (line.startsWith("$ cd ")) {
            val arg = line.substringAfter("$ cd ")
            t.addDirectory(arg)
        } else if (line.startsWith("$ ls")) {
            // Do nothing
        } else {
            val (content, name) = line.split(" ")
            if (content == "dir") {
                t.addFolderToCwd(name)
            } else {
                t.addFileToCwd(name, content.toLong())
            }
        }
    }

    t.cd()
    return t
}

fun <T> expected(actual: T, expected: T, message: String = "", verbose: Boolean = false) {
    if (actual != expected) error("NOK - $expected -> $actual | $message")
    if (verbose) println("OK  - $expected -> $actual | $message")
}