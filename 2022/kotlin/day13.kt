import java.io.File

private fun compare(left: String, right: String): Boolean {
    println("${left} ${right}")
    for (i in left.indices) {
        if (left[i] == '[' && right[i] == '[') {
            val l = extract(left.substring(i + 1, left.lastIndex))
            val r = extract(right.substring(i + 1, right.lastIndex))
            compare(l, r)
        }
    }
    return true
}


fun extract(list: String): String {
    var x = 0
    for (i in list.indices) {
        if (list[i] == '[') {
            x++
        } else if (list[i] == ']') {
            x--
            if (x == 0) {
                return list.substring(1, i)
            }
        }
    }
    return list
}

fun f(left: String, right: String) {
    val l = left.substring(1, left.lastIndex).split(",")
    val r = right.substring(1, right.lastIndex).split(",")
    for ((a, b) in l zip r) {
        println("comparing $a - $b")
    }
    println("${l} - ${r}")
}

fun main() {
    val sample = File("input/day13_sample.txt").bufferedReader()

    for (e in sample.lines().toList().chunked(3)) {
        val (left, right) = e
        f(left, right)
        println()
    }

}
