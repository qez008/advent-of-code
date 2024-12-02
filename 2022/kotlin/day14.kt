import linalg.*
import java.io.BufferedReader
import java.io.File
import java.lang.Exception
import java.lang.IndexOutOfBoundsException




fun findBounds(input: BufferedReader): List<Int> {
    var left = 500
    var right = 500
    var bottom = 0
    for (points in input.lines().map { it.split(" -> ") }) {
        for (point in points) {
            val (x, y) = point.split(",")
            bottom = maxOf(bottom, y.toInt())
            left = minOf(left, x.toInt())
            right = maxOf(right, x.toInt())
        }
    }
    return listOf(left - 160, right + 105, bottom + 2)
}

fun main() {
//    val sample = File("input/day14_sample.txt")
    val sample = File("input/day14.txt")

    val (left, right, bottom) = findBounds(sample.bufferedReader())
    val entryPoint = Vector2(0, 500 - left)

    val width = right - left + 1
    val cave = Array(bottom + 1) { Array(width) { '.' } }

    for (i in 0 until width) {
        cave[bottom][i] = '#'
    }
    cave[entryPoint] = '+'

    val addRockPath: (Vector2, Vector2) -> Unit = { a, b ->
        var p = a
        val dir = when {
            p.x < b.x -> Vector2(0, 1)
            p.x > b.x -> Vector2(0, -1)
            p.y < b.y -> Vector2(1, 0)
            p.y > b.y -> Vector2(-1, 0)
            else -> error("!!")
        }
        while (p != b) {
            cave[p] = '#'
            p += dir
        }
        cave[b] = '#'
    }

    for (points in sample.bufferedReader().lines().map { it.split(" -> ") }) {
        println(points)
        for (i in 1 until points.size) {
            println(points[i])
            val (x1, y1) = points[i - 1].split(",").map { it.toInt() }
            val (x2, y2) = points[i].split(",").map { it.toInt() }
            addRockPath(Vector2(y1, x1 - left), Vector2(y2, x2 - left))
        }
    }

    println(cave.joinToString("\n") { it.joinToString("") })

    fun dropSand(): Vector2 {
        val down = Vector2(1, 0)
        val downLeft = Vector2(1, -1)
        val downRight = Vector2(1, 1)

        var sand = entryPoint
        while (cave[entryPoint] == '+') {
            sand += when {
                cave[sand + down] == '.' -> down
                cave[sand + downLeft] == '.' -> downLeft
                cave[sand + downRight] == '.' -> downRight
                else -> break
            }
        }
        return sand
    }

    var i = 0
    try {
        while (cave[entryPoint] == '+') {
            cave[dropSand()] = 'o'
            i++
        }
        println("filled the cave after $i")
    } catch (e: Exception) {
        println("failed to drop sand after $i")
    }

    println(cave.joinToString("\n") { it.joinToString("") })
}