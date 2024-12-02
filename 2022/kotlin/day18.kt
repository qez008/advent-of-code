import linalg.*
import java.io.File
import java.lang.IndexOutOfBoundsException

fun main() {
    val sample = File("input/input.txt")
    val input = File("input/day18.txt")

    val n = 21
    val bounds = 0 until n

    val grid = Array(n) { Array(n) { Array(n) { '.' } } }

    var sum = 0
    for (line in input.bufferedReader().lines()) {
        val v = line.toVector3(",")
        grid[v] = '#'
        sum += 6
        for (d in Directions3D) {
            if (grid.getOrNull(v + d) == '#') {
                sum -= 2
            }
        }
    }
    println(sum)

    val mem = mutableMapOf<Vector3, Boolean>()

    fun canEscape(v: Vector3): Boolean {
        val visited = mutableSetOf<Vector3>()
        var vs = listOf(v)
        while (vs.isNotEmpty()) {
//            println(vs)
            val next = mutableListOf<Vector3>()
            for (u in vs) {
                if (u in visited) continue
                visited += u
                try {
                    next += Directions3D
                        .map { it + u }
                        .filter { grid[it] == '.' }
                        .filter { it !in visited }
                } catch (e: IndexOutOfBoundsException) {
//                    println("escaped")
                    return true
                }
            }
            vs = next
        }
//        println("did not escape")
        return false
    }

    var count = 0
    for (line in input.bufferedReader().lines()) {
        val v = line.toVector3(",")
        for (dir in Directions3D) {
            val u = v + dir
            if (grid.getOrNull(u) != '#' && canEscape(u)) {
                count++
            }
//            println(count)
        }
    }
    println(count)
}