import java.io.File

fun main() {
    val sample = File("input/day7_sample.txt")
    val input = File("input/day7.txt")

    val path = mutableListOf<String>()
    val result = mutableMapOf<String, Int>()

    for (line in input.bufferedReader().lines()) {
        when (line.take(4)) {
            "$ cd" -> {
                val (_, _, dir) = line.split(" ")
                when (dir) {
                    ".." -> path.removeLast()
                    else -> path.add((path.lastOrNull() ?: "") + "/$dir")
                }
            }
            "$ ls", "dir " -> continue
            else -> {
                val (size, _) = line.split(" ")
                for (dir in path) {
                    result[dir] = (result[dir] ?: 0) + size.toInt()
                }
            }
        }
    }

    val smallFolders = result.filter { (_, v) -> v <= 100000 }
    println(smallFolders.values.sum())

    val available = 70000000 - result["//"]!!
    val toDelete = 30000000 - available
    val smallestLargeEnoughDir = result.filter { it.value >= toDelete }.minBy { it.value }
    println(smallestLargeEnoughDir.value)
}
