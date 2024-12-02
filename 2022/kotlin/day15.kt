import linalg.*
import java.io.BufferedReader
import java.io.File
import kotlin.math.abs



private fun part1(reader: BufferedReader) {
    val answer = mutableMapOf<Int, Char>()
    val targetY = 2000000

    for ((sensorData, beaconData) in reader.lines()
        .map { it.split(": ") }) {

        val (x1, y1) = sensorData.drop("Sensor at ".length)
            .split(", ")
            .map { it.drop(2).toInt() }

        val (x2, y2) = beaconData.drop("closest beacon is at ".length)
            .split(", ")
            .map { it.drop(2).toInt() }

        if (y1 == targetY) {
            answer[x1] = 'S'
        }
        if (y2 == targetY) {
            answer[x2] = 'B'
        }

        val totalDistance = manhattanDistance(Vector2(x1, y1), Vector2(x2, y2))
        val yDistance = abs(targetY - y1)

        if (yDistance <= totalDistance) {
            val xDistance = totalDistance - yDistance
            println("$sensorData $beaconData")
            println("totalDist: $totalDistance, yDist: $yDistance, rem: $xDistance")
            for (k in 0..xDistance) {
                answer[x1 + k] = answer[x1 + k] ?: 'X'
                answer[x1 - k] = answer[x1 - k] ?: 'X'
            }
            println(List(30) { answer[it - 4] ?: '.' }.joinToString(""))
            println()

            for (k in 0..xDistance) {
                if (answer[x1 + k] == 'X') {
                    answer[x1 + k] = '#'
                }
                if (answer[x1 - k] == 'X') {
                    answer[x1 - k] = '#'
                }
            }
        }
    }
    println(answer.values.filter { it == '#' }.size)
}

private fun p2(reader: BufferedReader) {
    val bounds = 0 until 4000000

    val sensors = mutableListOf<Pair<Vector2, Int>>()

    for ((sensorData, beaconData) in reader.lines().map { it.split(": ") }) {

        val (x1, y1) = sensorData.drop("Sensor at ".length)
            .split(", ")
            .map { it.drop(2).toInt() }

        val (x2, y2) = beaconData.drop("closest beacon is at ".length)
            .split(", ")
            .map { it.drop(2).toInt() }

        val sensor = Vector2(x1, y1)
        val beacon = Vector2(x2, y2)

        sensors.add(sensor to manhattanDistance(sensor, beacon))
    }

    for ((sensorA, dist) in sensors) {
        for (i in 0..dist + 1) {
            val j = dist + 1 - i
            val dirs = listOf(
                Vector2(i, j),
                Vector2(-i, j),
                Vector2(i, -j),
                Vector2(-i, -j)
            )
            for (p in dirs.map { it + sensorA }) {
                if (p.x !in bounds || p.y !in bounds) {
                    continue
                }
                var here = true
                for ((sensorB, distB) in sensors) {
                    if (sensorA == sensorB) {
                        continue
                    }
                    if (manhattanDistance(p, sensorB) <= distB) {
                        here = false
                        break
                    }
                }
                if (here) {
                    println(4000000L * p.x + p.y)
                    return
                }
            }
        }
    }
}

fun main() {
    val sample = File("input/day15_sample.txt")
    val input = File("input/day15.txt")

    p2(input.bufferedReader())
}