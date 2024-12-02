import linalg.*
import java.io.File


open class Shape(val type: String, val points: List<Vector2>) {

    fun moveTo(position: Vector2): Shape {
        return Shape(type, points.map { it + position })
    }

    fun moveDown(): Shape {
        return Shape(type, points.map { it + Vector2(0, -1) })
    }

    fun moveRight(): Shape {
        return Shape(type, points.map { it + Vector2(1, 0) })
    }

    fun moveLeft(): Shape {
        return Shape(type, points.map { it + Vector2(-1, 0) })
    }
}


private class HLine : Shape("hline",
    listOf(Vector2(0, 0), Vector2(1, 0), Vector2(2, 0), Vector2(3, 0))
)

private class Plus : Shape("plus",
    listOf(
        Vector2(1, 2),
        Vector2(0, 1), Vector2(1, 1), Vector2(2, 1),
        Vector2(1, 0),
    )
)


private class Angle : Shape("angle",
    listOf(
        Vector2(2, 2),
        Vector2(2, 1),
        Vector2(2, 0), Vector2(1, 0), Vector2(0, 0)
    )
)

private class VLine : Shape("vline",
    listOf(Vector2(0, 0), Vector2(0, 1), Vector2(0, 2), Vector2(0, 3))
)

private class Square : Shape("square",
    listOf(
        Vector2(0, 0), Vector2(0, 1),
        Vector2(1, 0), Vector2(1, 1)
    )
)

fun main() {
    val w = 7
    val bounds = 0 until w
    val n = 2022
    var top = 4

    val chamber = mutableMapOf<Vector2, Char>()
    val history = mutableListOf<Pair<String, Int>>()

    val sample = File("input/day17_sample.txt")
    val input = File("input/day17.txt")

    val pattern = sample.bufferedReader().readLine()

    for (x in bounds) {
        chamber[Vector2(x, 0)] = '-'
    }

    fun printChamber(height: Int) {
        val arr = Array(height) { y -> Array(7) { x -> chamber[Vector2(x, y)] ?: '.' } }
        println(arr.reversed().joinToString("\n") { it.joinToString("") })
        println()
    }

    var time = 0
    for (i in 0 until (1704 + 1) * 2) {
        var shape = when (i % 5) {
            0 -> HLine()
            1 -> Plus()
            2 -> Angle()
            3 -> VLine()
            4 -> Square()
            else -> error("!!")
        }

        shape = shape.moveTo(Vector2(2, top))
        val sequence = mutableListOf<Char>()
        while (true) {
            val c = pattern[time++ % pattern.length]
            sequence += c
            val a = when (c) {
                '<' -> shape.moveLeft()
                '>' -> shape.moveRight()
                else -> error("!!")
            }
            if (a.points.all { p -> p.x in bounds && p !in chamber.keys }) {
                shape = a
            }

            val b = shape.moveDown()
            if (b.points.none { it in chamber.keys }) {
                shape = b
            } else {
                break
            }
        }

        var y = 0
        for (point in shape.points) {
            assert(point !in chamber.keys)
            chamber[point] = '#'
            y = maxOf(point.y, y)
        }
        top = maxOf(y + 4, top)

    }
    println(chamber.keys.maxOf { it.y })

}