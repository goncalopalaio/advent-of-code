typealias Grid = Array<Array<Int>>

val Grid.width: Int
    get() = get(0).size
val Grid.height: Int
    get() = size

fun Grid.at(x: Int, y: Int) = get(y)[x]

fun Grid.atOrNull(x: Int, y: Int): Int? {
    return if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
        null
    } else {
        get(y)[x]
    }
}

fun Grid.set(x: Int, y: Int, value: Int) {
    get(y)[x] = value
}

fun emptyGrid(x: Int, y: Int) = Array(y) {
    Array(x) { 0 }
}

fun Grid.print() {
    for (h in 0 until height) {
        for (w in 0 until width) {
            print("${at(w, h)}")
        }
        println()
    }
}


fun Grid.up(x: Int, y: Int, predicate: (cx: Int, cy: Int) -> Boolean) {
    for (cy in y downTo 0) if (!predicate(x, cy)) break
}

fun Grid.down(x: Int, y: Int, predicate: (cx: Int, cy: Int) -> Boolean) {
    for (cy in y until height) if (!predicate(x,cy)) break
}

fun Grid.left(x: Int, y: Int, predicate: (cx: Int, cy: Int) -> Boolean) {
    for (cx in x downTo 0) if (!predicate(cx, y)) break
}

fun Grid.right(x: Int, y: Int, predicate: (cx: Int, cy: Int) -> Boolean) {
    for (cx in x until width) if (!predicate(cx, y)) break
}
