package harness

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
