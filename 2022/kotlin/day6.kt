import java.io.File

fun main() {
    val sample = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
    val input = File("input/day6.txt").bufferedReader().readLine()
    val messageSize = 14

    val answer = messageSize.until(input.length).first { i ->
        input.subSequence(i - messageSize, i).toSet().size == messageSize
    }

    println(answer)
}

