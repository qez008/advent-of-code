import linalg.*
import java.io.File
import kotlin.math.abs
import kotlin.math.sign


fun main() {
    val sample = File("input/day9_sample.txt").bufferedReader().lines()
    val input = File("input/day9.txt").bufferedReader().lines()

    var head = Vector2(4, 0)
    val tail = MutableList(9) { head }

    val uniqueLocations = mutableSetOf(tail.last())

    val move: (Vector2, String) -> Vector2 = { h, s ->
        h + when (s) {
            "L" -> Vector2(0, -1)
            "R" -> Vector2(0, 1)
            "U" -> Vector2(-1, 0)
            "D" -> Vector2(1, 0)
            else -> error("!!")
        }
    }

    val follow: (Vector2, Vector2) -> Vector2 = { h, t ->
        val (xDistance, yDistance) = h - t
        if (abs(xDistance) > 1 || abs(yDistance) > 1) {
            val xDirection = sign(xDistance.toDouble()).toInt()
            val yDirection = sign(yDistance.toDouble()).toInt()
            t + Vector2(xDirection, yDirection)
        } else {
            t
        }
    }

    for (line in input) {
        val (dir, times) = line.split(" ")
        repeat(times.toInt()) {
            head = move(head, dir)
            var previous = head
            for (i in tail.indices) {
                tail[i] = follow(previous, tail[i])
                previous = tail[i]
            }
            uniqueLocations += tail.last()
        }
    }

    println(uniqueLocations.size)
}