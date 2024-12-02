import java.io.File

val sample = """
    A Y
    B X
    C Z
""".trimIndent()

const val LOST = 0
const val DRAW = 3
const val WON = 6

sealed class RockPaperScissor(val value: Int) {

    abstract fun scoreAgainst(other: RockPaperScissor): Int
    abstract fun beatenBy(): RockPaperScissor
    abstract fun beats(): RockPaperScissor

    object Rock : RockPaperScissor(1) {
        override fun scoreAgainst(other: RockPaperScissor): Int {
            return when (other) {
                is Rock -> DRAW
                is Paper -> LOST
                is Scissor -> WON
                else -> error("should never happen")
            }
        }
        override fun beatenBy() = Paper
        override fun beats() = Scissor
    }

    object Paper : RockPaperScissor(2) {
        override fun scoreAgainst(other: RockPaperScissor): Int {
            return when (other) {
                is Rock -> WON
                is Paper -> DRAW
                is Scissor -> LOST
                else -> error("should never happen")
            }
        }
        override fun beatenBy() = Scissor
        override fun beats() = Rock
    }

    object Scissor : RockPaperScissor(3) {
        override fun scoreAgainst(other: RockPaperScissor): Int {
            return when (other) {
                is Rock -> LOST
                is Paper -> WON
                is Scissor -> DRAW
                else -> error("should never happen")
            }
        }
        override fun beatenBy() = Rock
        override fun beats() = Paper
    }

    companion object {
        fun fromChar(x: Char): RockPaperScissor {
            return when (x) {
                'A', 'X' -> Rock
                'B', 'Y' -> Paper
                'C', 'Z' -> Scissor
                else -> error("undefined character")
            }
        }
    }
}

fun main() {
    var total = 0
    File("input/day2.txt").forEachLine { line ->
        val opponent = RockPaperScissor.fromChar(line[0])
        val hand = when (line[2]) {
            'X' -> opponent.beats()
            'Y' -> opponent
            'Z' -> opponent.beatenBy()
            else -> error("???")
        }
        val outcome = hand.scoreAgainst(opponent) + hand.value
        total += outcome
    }
    println(total)
}
