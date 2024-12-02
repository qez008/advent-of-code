import java.io.File

fun findCommonCharacter(a: String, b: String): Char {
    for (x in a) {
        if (x in b) return x
    }
    error("no common character")
}

fun priority(x: Char) = if (x in 'a'..'z') (x.code - 'a'.code + 1) else (x.code - 'A'.code + 27)

fun partOne() {
    File("input/day3.txt").bufferedReader().lines().reduce { line, sum ->
        val common = findCommonCharacter(line.take(line.length / 2), line.drop(line.length / 2))
        sum + priority(common)
    }.also(::println)
}


fun findBadge(a: String, b: String, c: String): Char {
    for (x in a) {
        if (x in b && x in c) return x
    }
    error("badge not found")
}

fun partTwo() {
    File("input/day3.txt").bufferedReader().lines().toList().chunked(3) { group ->
        val (a, b, c) = group
        val badge = findBadge(a, b, c)
        priority(badge)
    }.sum().also(::println)
}


fun main() {
    partTwo()
}


