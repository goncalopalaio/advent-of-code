import Debug.log
import kotlin.system.measureTimeMillis

// Add general functions here

object Debug {
    var DEBUG = true

    fun log(message: String) {
        if (DEBUG) println(message)
    }
}

inline fun measurePrint(message: String, block: () -> Int) {
    val result: Int
    val time = measureTimeMillis {
        result = block()
    }

    val formatted = "%-16s | %-32s | %-16s".format("$result", message, "${time}ms")
    println(formatted)
}