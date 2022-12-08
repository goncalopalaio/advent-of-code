package twentytwentyone

import java.util.ArrayList

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
