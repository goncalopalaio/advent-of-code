import java.util.ArrayList
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

inline fun <T> runProblem(
    message: String,
    additionalRuns: Int = 0,
    warmUp: Boolean = true,
    expected: T? = null,
    block: () -> T
): Pair<String, Double> {
    val result: T
    val mainTime = measureTimeMillis {
        result = block()
    }

    if (expected != null) {
        if (result != expected) {
            throw RuntimeException("Unexpected result. expected >$expected< got >$result<")
        }
    }

    val partialTimes = mutableListOf<Long>()
    if (additionalRuns >= 1) {
        // Does warming up really matter? -- Apparently it does.
        if (warmUp) {
            for (run in 0 until additionalRuns) {
                block()
            }
        }

        for (run in 0 until additionalRuns) {
            val partialTime = measureNanoTime {
                block()
            }
            partialTimes.add(partialTime)
        }
    }


    val formatted = if (partialTimes.isEmpty()) {
        "%-16s | %-32s | %-8s".format("$result", message, "${mainTime}ms")
    } else {
        val avgMillis = (partialTimes.average() / 1e6)
        "%-16s | %-32s | %-8s | %-8s | %-8s".format(
            "$result",
            message,
            "${mainTime}ms",
            "runs=${partialTimes.size}",
            "avg=${avgMillis} ms"
        )
    }

    println(formatted)

    return if (partialTimes.isEmpty()) {
        Pair(message, mainTime.toDouble())
    } else {
        Pair(message, (partialTimes.average() / 1e6))
    }
}


/**
 * Generate all permutations
 * https://www.quickperm.org/
 */
private fun <T> quickPerm(a: Array<T>): ArrayList<Array<T>> {
    val permutations = ArrayList<Array<T>>()

    val n = a.size
    val p = (0..(n + 1)).toList().toTypedArray()
    var i = 1
    while (i < n) {
        p[i] -= 1
        val j = if ((i % 2) == 1) {
            p[i]
        } else {
            0
        }

        // swap
        val temp = a[i]
        a[i] = a[j]
        a[j] = temp

        permutations.add(a.copyOf())

        i = 1
        while (p[i] == 0) {
            p[i] = i
            i += 1
        }
    }

    return permutations
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


