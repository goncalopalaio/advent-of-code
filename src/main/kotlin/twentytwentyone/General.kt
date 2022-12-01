package twentytwentyone

import runProblem
import java.util.ArrayList

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


