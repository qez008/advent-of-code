import java.io.File

fun main() {
    val input = File("input/day20.txt")
    val encryptedMessage = input.bufferedReader().lines()
        .map { it.toInt() * 811589153L }
        .toList().toMutableList()

    val message = encryptedMessage.zip(encryptedMessage.indices).toMutableList()

    repeat(10) { _ ->
        for (i in message.indices) {
            val index = message.indexOfFirst { (_, order) -> order == i }
            val x = message.removeAt(index)
            val newIndex = Math.floorMod(index + x.first, message.size)
            message.add(newIndex, x)
        }
    }

    val decryptedMessage = message.map(Pair<Long, Int>::first)
    val zeroIndex = decryptedMessage.indexOf(0)

    val answer = listOf(1000, 2000, 3000)
        .map { i -> decryptedMessage[(zeroIndex + i) % message.size]  }

    println("$answer ${answer.sum()}")
}
