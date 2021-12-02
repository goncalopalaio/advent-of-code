import java.util.*
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

// Add general functions here

object Debug {
    var DEBUG = true

    fun log(message: String) {
        if (DEBUG) println(message)
    }
}

inline fun <T> measurePrint(
    message: String,
    additionalRuns: Int = 0,
    warmUp: Boolean = true,
    block: () -> T
): Pair<String, Double> {
    val result: T
    val mainTime = measureTimeMillis {
        result = block()
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
