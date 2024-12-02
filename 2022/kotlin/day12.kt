import linalg.*
import java.io.File

fun canClimb(a: Vector2, b: Vector2, heightmap: List<String>): Boolean {
    val getHeight: (Vector2) -> Char = { v ->
        when (val h = heightmap[v]) {
            'S' -> 'a'
            'E' -> 'z'
            else -> h
        }
    }
    return getHeight(b) in 'a'..getHeight(a).inc()
}

fun bfs(start: Vector2, heightmap: List<String>, width: Int, height: Int): Int {
    val visited = heightmap.map { BooleanArray(it.length) { false } }.toMutableList()
    var i = 0
    var layer = listOf(start)

    while (layer.isNotEmpty()) {
        val nextLayer = mutableListOf<Vector2>()
        for (node in layer) {
            when {
                heightmap[node] == 'E' -> return i
                visited[node] -> continue
                else -> {
                    nextLayer += Directions2D.map { it + node }
                        .filter { (y, x) -> y in 0 until height && x in 0 until width }
                        .filter { canClimb(node, it, heightmap) }
                        .filter { !visited[it] }

                    visited[node] = true
                }
            }
        }

        layer = nextLayer
        i++
    }

    return Int.MAX_VALUE
}

fun main() {
    val sample = File("input/day12_sample.txt").bufferedReader()
    val input = File("input/day12.txt").bufferedReader()

    val heightmap = input.lines().toList()
    val width = heightmap.first().length
    val height = heightmap.size

    val start = Vector2(20, 0)
    println(heightmap[start])

    val answer1 = bfs(start, heightmap, width, height)

    println(answer1)
//
//    val answer2 = (0 until height).map { y -> (0 until width).map { x -> Vector2(y, x) } }.flatten()
//        .filter { heightmap[it] == 'a' }
//        .minOf { bfs(it, heightmap, width, height) }

//    println(answer2)
}
