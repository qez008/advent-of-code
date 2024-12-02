import java.io.File

private data class Monkey(
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: (Long) -> Int,
)


fun main() {
    val sample = File("input/day11_sample.txt").bufferedReader().lines().toList()
    val input = File("input/day11.txt").bufferedReader().lines().toList()

    val monkeys = input.chunked(7)
        .map { monkeyData: List<String> ->
            val items = monkeyData[1].removePrefix("  Starting items: ")
                .split(",")
                .map { it.trim().toLong() }
                .toMutableList()

            val (a, y) = monkeyData[2].removePrefix("  Operation: new = old ")
                .split(" ")
            val op: (Long, Long) -> Long = if (a == "*") Long::times else Long::plus
            val operation: (Long) -> Long = { x ->
                val largeNumber = op(x, if (y == "old") x else y.toLong())
                largeNumber % 223092870L
            }

            val testNumber = monkeyData[3].removePrefix("  Test: divisible by ").toLong()
            val monkeyA = monkeyData[4].removePrefix("    If true: throw to monkey ").toInt()
            val monkeyB = monkeyData[5].removePrefix("    If false: throw to monkey ").toInt()
            val test = { x: Long -> if (x % testNumber == 0L) monkeyA else monkeyB }

            Monkey(items, operation, test)
        }

    val monkeyBusiness = LongArray(monkeys.size) { 0L }

    repeat(10000) {

        for ((i, monkey) in monkeys.withIndex()) {
            for (item in monkey.items) {
                monkeyBusiness[i]++
                val new = monkey.operation(item)
                val throwTo = monkey.test(new)
                monkeys[throwTo].items.add(new)
            }
            monkey.items.clear()
        }

    }

    println(monkeyBusiness.toList())

    val (a, b) = monkeyBusiness.sortedDescending().take(2)
    println(a * b)
}

