package linalg

import java.lang.IndexOutOfBoundsException

typealias Vector3 = Triple<Int, Int, Int>

val Vector3.x
    get() = this.first

val Vector3.y
    get() = this.second

val Vector3.z
    get() = this.third

operator fun Vector3.plus(other: Vector3) = Vector3(x + other.x, y + other.y, z + other.z)

operator fun Vector3.minus(other: Vector3) = Vector3(x - other.x, y - other.y, z - other.z)

operator fun Vector3.times(k: Int) = Vector3(x * k, y * k, z * k)

operator fun Vector3.times(other: Vector3) = Vector3(x * other.x, y * other.y, z * other.z)


// Collections getters

operator fun Array<Array<BooleanArray>>.get(v: Vector3) = this[v.z][v.y][v.x]

fun Array<Array<BooleanArray>>.getOrNull(v: Vector3) = try {
    this[v.z][v.y][v.x]
} catch (e: IndexOutOfBoundsException) {
    null
}

fun <T> Array<Array<Array<T>>>.getOrNull(v: Vector3) = try {
    this[v.z][v.y][v.x]
} catch (e: IndexOutOfBoundsException) {
    null
}

operator fun <T> Array<Array<Array<T>>>.get(v: Vector3) = this[v.z][v.y][v.x]


// Collections setters

operator fun Array<Array<BooleanArray>>.set(v: Vector3, value: Boolean) {
    this[v.z][v.y][v.x] = value
}

operator fun <T> Array<Array<Array<T>>>.set(v: Vector3, value: T) {
    this[v.z][v.y][v.x] = value
}


// Utility

fun String.toVector3(delimiter: String): Vector3 {
    val (x, y, z) = this.split(delimiter).map { it.toInt() }
    return Vector3(x, y, z)
}

object Directions3D : Iterable<Vector3> {

    @JvmStatic
    val FORWARDS = Vector3(1, 0, 0)

    @JvmStatic
    val BACKWARDS = Vector3(-1, 0, 0)

    @JvmStatic
    val UP = Vector3(0, -1, 0)

    @JvmStatic
    val DOWN = Vector3(0, 1, 0)

    @JvmStatic
    val LEFT = Vector3(0, 0, -1)

    @JvmStatic
    val RIGHT = Vector3(0, 0, 1)

    override fun iterator(): Iterator<Vector3> {
        return listOf(UP, DOWN, RIGHT, LEFT, BACKWARDS, FORWARDS).iterator()
    }

}


infix operator fun Vector3.contains(range: Collection<Int>) = x in range && y in range && z in range

fun Vector3.inBounds(width: IntRange, height: IntRange, depth: IntRange) =
    x in width && y in height && z in depth

