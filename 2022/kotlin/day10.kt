import java.io.File
import kotlin.math.abs

fun main() {
    val sample = File("input/day10_sample.txt").bufferedReader().lines()
    val input = File("input/day10.txt").bufferedReader().lines()

    val cycles = listOf(20, 60, 100, 140, 180, 220)

    var cycle = 1
    var x = 1
    var sum = 0

    val grid = List(6) { CharArray(40) { '.' } }

    fun execute() {
        val row = (cycle - 1) / 40
        val col = (cycle - 1) % 40

        if (abs(x - col) <= 1) {
            grid[row][col] = '#'
        }

        cycle += 1

        if (cycle in cycles) {
            sum += x * cycle
        }
    }

    for (instruction in input) {
        execute()
        if (instruction.startsWith("addx")) {
            execute()
            val y = instruction.drop(5).toInt()
            x += y
        }
    }

    println(sum)

    for (row in grid) {
        println(row.joinToString(separator = ""))
    }
}