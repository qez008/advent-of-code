package day2b

import java.io.File

val score: (Char) -> Int = { hand -> hand.code - 64 }

val outcome: (Char) -> Int = { outcome -> (outcome.code - 88) * 3 }

val yourHand: (Int, Int) -> Int = { desiredOutcome, opponent ->
    when (desiredOutcome) {
        0 -> if (opponent == 1) 3 else opponent - 1
        3 -> opponent
        6 -> if (opponent == 3) 1 else opponent + 1
        else -> error("!!!")
    }
}

fun main() {
    var total = 0
    File("input/day2.txt").forEachLine { line ->
        val opponent = score(line[0])
        val outcome = outcome(line[2])
        total += yourHand(outcome, opponent)
    }
    println(total)
}