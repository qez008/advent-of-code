package linalg

import java.lang.IndexOutOfBoundsException
import kotlin.math.abs


typealias Vector2 = Pair<Int, Int>

val Vector2.x
    get() = this.first

val Vector2.y
    get() = this.second

fun manhattanDistance(a: Vector2, b: Vector2): Int {
    return abs(a.x - b.x) + abs(a.y - b.y)
}

operator fun Vector2.plus(other: Vector2) = Vector2(first + other.first, second + other.second)
operator fun Vector2.minus(other: Vector2) = Vector2(first - other.first, second - other.second)
operator fun List<String>.get(v: Vector2) = this[v.first][v.second]

operator fun List<BooleanArray>.get(v: Vector2) = this[v.first][v.second]

operator fun MutableList<BooleanArray>.set(v: Vector2, value: Boolean) {
    this[v.first][v.second] = value
}


object Directions2D : Iterable<Vector2> {


    @JvmStatic
    val UP = Vector2(1, 0)

    @JvmStatic
    val DOWN = Vector2(-1, 0)

    @JvmStatic
    val LEFT = Vector2(0, -1)

    @JvmStatic
    val RIGHT = Vector2(0, 1)

    override fun iterator(): Iterator<Vector2> {
        return listOf(UP, DOWN, LEFT, RIGHT).iterator()
    }

}

operator fun <T> Array<Array<T>>.set(v: Vector2, value: T) {
    this[v.y][v.x] = value
}

operator fun <T> Array<Array<T>>.get(v: Vector2): T {
    return this[v.y][v.x]
}

fun <T> Array<Array<T>>.getOrNull(v: Vector2) = try {
    this[v.y][v.x]
} catch (e: IndexOutOfBoundsException) {
    null
}



