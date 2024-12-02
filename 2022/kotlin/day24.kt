package dat24

import linalg.*
import java.io.File

data class Blizzard(val direction: Vector2, var position: Vector2)

fun main() {
    val input = File("input/day24.txt")

    val map = input.bufferedReader().lines()
        .map { it.toList().toTypedArray() }
        .toList().toTypedArray()

    println(map.joinToString("\n") { it.joinToString("") })

    val start = Vector2(map.first().indexOfFirst { it == '.' }, 0)
    val finish = Vector2(map.last().indexOfFirst { it == '.' }, map.size - 1)

    println("start $start, finish: $finish")

    val blizzards = mutableListOf<Blizzard>()

    for (y in map.indices) {
        for (x in map[y].indices) {
            val direction = when (map[y][x]) {
                '>' -> Vector2(1, 0)
                'v' -> Vector2(0, 1)
                '<' -> Vector2(-1, 0)
                '^' -> Vector2(0, -1)
                else -> continue
            }
            val b = Blizzard(direction, Vector2(x, y))
            blizzards.add(b)
            map[y][x] = '.'
        }
    }

    fun update() {
        for (b in blizzards) {
            var (x, y) = b.position + b.direction
            if (x == 0) {
                x = map.first().size - 2
            } else if (x == map.first().size - 1) {
                x = 1
            } else if (y == 0) {
                y = map.size - 2
            } else if (y == map.size - 1) {
                y = 1
            }
            b.position = Vector2(x, y)
        }
    }

    fun printMap(elves: List<Vector2>) {
        for (elf in elves) {
            map[elf] = 'E'
        }
        for (b in blizzards) {
            val char = when (b.direction) {
                Vector2(1, 0) -> '>'
                Vector2(0, 1) -> 'v'
                Vector2(-1, 0) -> '<'
                Vector2(0, -1) -> '^'
                else -> error("!")
            }
            map[b.position] = when {
                map[b.position] in ".#E" -> char
                map[b.position] in "<>v^" -> '2'
                else -> (map[b.position].digitToInt() + 1).digitToChar()
            }
        }
        println()
        println(map.joinToString("\n") { it.joinToString("") })
        for (elf in elves) {
            map[elf] = '.'
        }
        for (b in blizzards) {
            map[b.position] = '.'
        }
    }

    fun bfs(start: Vector2, finish: Vector2): Int {
        val queue = mutableListOf(start)
        var depth = 0
        while (queue.isNotEmpty()) {
            val next = mutableSetOf<Vector2>()
            for (position in queue) {
                if (position == finish) {
                    return depth
                }
                if (blizzards.find { b -> b.position == position } != null) {
                    continue
                }
                next += position
                next += Directions2D.map { position + it }
                    .filter { (x, y) -> y in map.indices && x in map.first().indices }
                    .filter { map[it] == '.' }
            }
            queue.clear()
            queue.addAll(next)
            update()
            depth++
        }

        return -1
    }

    print(bfs(start, finish) + bfs(finish, start) + bfs(start, finish))
}