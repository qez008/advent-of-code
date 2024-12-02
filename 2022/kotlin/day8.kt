import java.io.File
import kotlin.system.measureTimeMillis

private fun partOne(grid: List<List<Int>>) {
    val views = MutableList(4) { grid.map { it.toIntArray() }.toTypedArray() }

    val right = 0
    val down = 1
    val left = 2
    val up = 3

    for (y in grid.indices) {
        for (x in 1 until grid.size) {

            val rightView = views[right]
            rightView[y][x] = maxOf(rightView[y][x], rightView[y][x - 1])

            val downView = views[down]
            downView[x][y] = maxOf(downView[x][y], downView[x - 1][y])

            val x2 = grid.size - 1 - x

            val leftView = views[left]
            leftView[y][x2] = maxOf(leftView[y][x2], leftView[y][x2 + 1])

            val upView = views[up]
            upView[x2][y] = maxOf(upView[x2][y], upView[x2 + 1][y])
        }
    }

    var answer = (grid.size - 1) * 4

    for (y in 1 until grid.size - 1) {
        for (x in 1 until grid.size - 1) {

            val height = grid[y][x]
            if (height > views[right][y][x - 1]
                || height > views[down][y - 1][x]
                || height > views[left][y][x + 1]
                || height > views[up][y + 1][x]
            ) {
                answer++
            }
        }
    }

    println(answer)
}


private fun partOneB(grid: List<List<Int>>) {
    var answer = (grid.size - 1) * 4

    val range = 1 until grid.size - 1
    for (y in range) {
        for (x in range) {
            val height = grid[y][x]

            val left = (x + 1).until(grid.size).all { grid[y][it] < height }
            val right = (x - 1).downTo(0).all { grid[y][it] < height }
            val down = (y + 1).until(grid.size).all { grid[it][x] < height }
            val up = (y - 1).downTo(0).all { grid[it][x] < height }

            if (left || right || down || up) answer++
        }
    }

    println(answer)
}

private fun partTwo(grid: List<List<Int>>) {
    var answer2 = 2

    for (y in 1 until grid.size - 1) {
        for (x in 1 until grid.size - 1) {
            val height = grid[y][x]
            var a = 0
            for (i in y - 1 downTo 0) {
                a++
                if (grid[i][x] >= height) break
            }
            var b = 0
            for (i in y + 1 until grid.size) {
                b++
                if (grid[i][x] >= height) break

            }
            var c = 0
            for (i in x - 1 downTo 0) {
                c++
                if (grid[y][i] >= height) break

            }
            var d = 0
            for (i in x + 1 until grid.size) {
                d++
                if (grid[y][i] >= height) break

            }
            answer2 = maxOf(answer2, a * b * c * d)
        }
    }

    println(answer2)
}

fun main() {
    val sample = File("input/day8_sample.txt")
    val input = File("input/day8.txt")

    val grid = input.bufferedReader().lines()
        .map { line -> line.map { it.digitToInt() } }
        .toList()

    measureTimeMillis {
        partOne(grid)
    }.also { println(it) }
    measureTimeMillis {
        partOneB(grid)
    }.also { println(it) }
    partTwo(grid)
}

