package day16

import java.io.File
import java.util.stream.Stream

private data class Valve(
    val flow: Int,
    val connections: List<String>
)

private data class Tunnel(
    val flow: Int,
    val connections: Map<String, Int>
)

private fun bfs(start: String, valves: Map<String, Valve>): Map<String, Int> {
    val explore = mutableListOf(start)
    val distances = mutableMapOf<String, Int>()
    var depth = 0
    while (explore.isNotEmpty()) {
        val next = mutableListOf<String>()
        for (e in explore) {
            if (e in distances.keys) {
                continue
            }
            distances[e] = depth
            for (c in valves[e]!!.connections) {
                if (c !in distances.keys) {
                    next += c
                }
            }
        }
        explore.clear()
        explore.addAll(next)
        depth++
    }
    return distances
}


private fun processInput(lines: Stream<String>): Map<String, Valve> {
    return buildMap {
        for (line in lines) {
            val (a, b) = line.split("; ")
            val (id, rate) = a.split(" ", "=").slice(listOf(1, 5))
            val connections = when {
                "valves" in b -> b.removePrefix("tunnels lead to valves ")
                else -> b.removePrefix("tunnel leads to valve ")
            }
            put(id, Valve(rate.toInt(), connections.split(", ")))
        }
    }
}

private fun optimizeInput(valves: Map<String, Valve>): Map<String, Tunnel> {
    return buildMap {
        for ((id, valve) in valves) {
            val connections = bfs(id, valves)
                .filterKeys { it != id }
                .filterKeys { valves[it]!!.flow != 0 }
            if (id == "AA" || valve.flow != 0) {
                put(id, Tunnel(valve.flow, connections))
            }
        }
    }
}

private fun part1(tunnels: Map<String, Tunnel>) {
    val mem = HashMap<Triple<String, Int, Set<String>>, Long>()

    fun solve(v: String, tunnels: Map<String, Tunnel>, enabled: Set<String>, minutesLeft: Int): Long {
        if (minutesLeft <= 1) {
            return 0L
        }
        val key = Triple(v, minutesLeft, enabled)
        if (key in mem.keys) {
            return mem[key]!!
        }

        val tunnel = tunnels[v]!!
        var maxValue = 0L
        val release = tunnel.flow * (minutesLeft - 1)
        for ((u, w) in tunnel.connections) {
            val x1 = solve(u, tunnels, enabled, minutesLeft - w)
            maxValue = maxOf(maxValue, x1)
            if (v !in enabled) {
                val x2 = solve(u, tunnels, enabled + v, minutesLeft - w - 1)
                maxValue = maxOf(maxValue, x2 + release)
            }
        }

        mem[key] = maxValue

        return maxValue
    }

    println(solve("AA", tunnels, setOf("AA"), 30))
}

private data class Key(
    val nodeA: Int,
    val nodeB: Int,
    val timeA: Int,
    val timeB: Int,
    val enabled: Set<Int>
)

private fun part2(tunnels: Map<String, Tunnel>) {

    val ids = (tunnels.keys zip tunnels.keys.indices).toMap()

    val mem = HashMap<Key, Long>()

    fun solve(v: String, p: String, tunnels: Map<String, Tunnel>, enabled: Set<Int>, t1: Int, t2: Int): Long {
        if (t1 <= 1 && t2 <= 1) {
            return 0L
        }
        val idv = ids[v]!!
        val idp = ids[p]!!
        val key = Key(idv, idp, t1, t2, enabled)

        if (key in mem.keys) {
            return mem[key]!!
        }

        var maxRelease = 0L

        val tunnel1 = tunnels[v]!!
        val tunnel2 = tunnels[p]!!

        val release1 = if (t1 >= 1) tunnel1.flow * (t1 - 1) else 0
        val release2 = if (t2 >= 1) tunnel2.flow * (t2 - 1) else 0

        for ((u, w1) in tunnel1.connections) {
            for ((q, w2) in tunnel2.connections) {
                if (v != p && idv !in enabled && idp !in enabled) {
                    val opt1 = solve(u, q, tunnels, enabled + setOf(idv, idp), t1 - 1 - w1, t2 - 1 - w2)
                    maxRelease = maxOf(maxRelease, opt1 + release1 + release2)
                }
                if (idv !in enabled) {
                    val opt2 = solve(u, q, tunnels, enabled + idv, t1 - 1 - w1, t2 - w2)
                    maxRelease = maxOf(maxRelease, opt2 + release1)
                }
                if (idp !in enabled) {
                    val opt3 = solve(u, q, tunnels, enabled + idp, t1 - w1, t2 - 1 - w2)
                    maxRelease = maxOf(maxRelease, opt3 + release2)
                }

                val opt4 = solve(u, q, tunnels, enabled, t1 - w1, t2 - w2)
                maxRelease = maxOf(maxRelease, opt4)
            }
        }
        mem[key] = maxRelease
        return maxRelease
    }

    println(solve("AA", "AA", tunnels, setOf(ids["AA"]!!), 26, 26))

}

fun main() {
    val sample = File("input/day16_sample.txt")
    val input = File("input/day16.txt")

    val tunnels = optimizeInput(processInput(input.bufferedReader().lines()))

    tunnels.forEach { println(it) }
    println()
    part1(tunnels)
    part2(tunnels)

}
