import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val cols = mutableListOf<Int>()
    var amount = 0
    File("input/day1.txt").bufferedReader()
        .forEachLine { line ->
            if (line == "") {
                cols.add(amount)
                amount = 0
            } else {
                amount += line.toInt()
            }
        }
    val answer: Int
    val time = measureTimeMillis {
//        answer = cols.sortedDescending().take(3).sum()
        answer = top3(cols).sum()
    }
    println(answer)
    println(time)

    assert(answer == 212489)
}


fun top3(xs: List<Int>): List<Int> {
    val pivot = xs.random()
    val upper = mutableListOf<Int>()
    val lower = mutableListOf<Int>()

    for (e in xs) {
        if (e < pivot) {
            lower.add(e)
        } else {
            upper.add(e)
        }
    }

    return when {
        upper.size == 3 -> upper
        upper.size <= 3 -> top3(lower.take(3 - upper.size) + upper)
        upper.size < 6 -> upper.sortedDescending().take(3)
        else -> top3(upper)
    }
}