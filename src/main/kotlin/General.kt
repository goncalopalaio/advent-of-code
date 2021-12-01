// Add general functions here

object Debug {
    var DEBUG = true

    fun log(message: String) {
        if (DEBUG) println(message)
    }
}