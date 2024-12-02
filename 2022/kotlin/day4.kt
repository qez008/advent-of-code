import java.io.File

fun day4A() {
    val sample = """
        2-4,6-8
        2-3,4-5
        5-7,7-9
        2-8,3-7
        6-6,4-6
        2-6,4-8
    """.trimIndent()
//    sample.split("\n").map { line ->
    File("input/day4.txt").bufferedReader().lines().toList().map { line ->
        val (a, b, c, d) = line.split(",", "-").map(String::toInt)
        if (a <= c && b >= d || c <= a && d >= b) 1 else 0
    }.sum().also(::println)
}

fun day4B() {
    File("input/day4.txt").bufferedReader().lines().toList().sumOf { line ->
        val (a, b, c, d) = line.split(",", "-").map(String::toInt)
        if ((a..b).any { (c..d).contains(it) }) 1L else 0L
    }.also(::println)
}

fun main() {
    day4A()
}