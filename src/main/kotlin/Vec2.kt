class Vec2<out A, out B>(
    val x: A,
    val y: B
) {
    override fun toString(): String {
        return "($x, $y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vec2<*, *>

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x?.hashCode() ?: 0
        result = 31 * result + (y?.hashCode() ?: 0)
        return result
    }


}

infix fun <A, B> A.v2(that: B): Vec2<A, B> = Vec2(this, that)

val Pair<Int, Int>.x: Int
    get() = first

val Pair<Int, Int>.y: Int
    get() = second

fun Pair<Int, Int>.toVec2() = Vec2(first, second)