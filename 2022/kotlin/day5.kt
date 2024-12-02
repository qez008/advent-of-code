import java.io.File

fun main() {

    val initialHeight = 8
    val stacks = List(9) { mutableListOf<Char>() }

    val input = File("input/day5.txt").bufferedReader().lines().toList()

    for (line in input.take(initialHeight)) {
        for (i in stacks.indices) {
            val x = line.getOrElse(1 + 4 * i) { ' ' }
            if (x != ' ') {
                stacks[i].add(0, x)
            }
        }
    }

    for (line in input.drop(2 + initialHeight)) {
        val (n, from, to) = line.split(" ")
            .slice(listOf(1, 3, 5))
            .map(String::toInt)
        stacks[to - 1].addAll(stacks[from - 1].takeLast(n))
        repeat(n) {
            stacks[from - 1].removeLast()
        }
    }

    println(stacks.map(MutableList<Char>::last).joinToString(separator = ""))
}