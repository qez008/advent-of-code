package day21

import java.io.File
import java.math.BigInteger


fun main() {
    val input = File("input/day21.txt")

    val yellingMonkeys = input.bufferedReader().lines()
        .map { line -> line.split(": ") }
        .map { (a, b) -> a to b.split(" ") }
        .toList().toMap()

    var min = Long.MIN_VALUE.toBigInteger()
    var max = Long.MAX_VALUE.toBigInteger()
    var guess = (0L).toBigInteger()

    fun rec(id: String): BigInteger {
        val job = yellingMonkeys[id]!!
        return when {
            id == "humn" -> guess
            job.size == 1 -> job.single().toBigInteger()
            else -> {
                val (a, op, b) = job
                val f: (BigInteger, BigInteger) -> BigInteger = when (op) {
                    "+" -> { x, y -> x + y }
                    "-" -> { x, y -> x - y }
                    "*" -> { x, y -> x * y }
                    "/" -> { x, y -> x / y }
                    else -> error("!!")
                }
                f(rec(a), rec(b))
            }
        }
    }

    while (true) {
        println("guess $guess")
        val (a, _, b) = yellingMonkeys["root"]!!
        val left = rec(a)
        println("left:  $left")
        val right = rec(b)
        println("right: $right")

        println("diff ${left - right}")

        if (left == right) {
            println("here!! $guess")
            break
        }
        if (left > right) {
            min = guess
        } else {
            max = guess
        }
        guess = min + (max - min) / BigInteger.TWO
        println()
    }


    for (i in 0 until 10) {
        guess = (3887609741189 + i).toBigInteger()
        println("guess $guess")
        val (a, _, b) = yellingMonkeys["root"]!!
        val left = rec(a)
        val right = rec(b)

        println("diff ${left - right}")
        println()
    }



}
