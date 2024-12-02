package day22

import linalg.*
import java.io.File

private const val RIGHT = 0
private const val DOWN = 1
private const val LEFT = 2
private const val UP = 3


typealias CubeWorld = Map<String, Array<Array<Char>>>

private data class State(val position: Vector2, val side: String, val facing: Int)


private fun traverseSides(state: State): State {
    val (position, side, facing) = state
    if (side == "top") {
        val (newSide, newPosition) = when (facing) {
            RIGHT -> "right" to Vector2(position.y, 0)
            DOWN -> "front" to Vector2(position.x, 0)
            LEFT -> "left" to Vector2(position.y, 0)
            UP -> "back" to Vector2(position.x, 0)
            else -> error("")
        }
        return State(newPosition, newSide, 1)
    }
    if (side == "bottom") {
        val (newSide, newPosition) = when (facing) {
            RIGHT -> "right" to Vector2(position.y, 49)
            DOWN -> "back" to Vector2(position.x, 49)
            LEFT -> "left" to Vector2(position.y, 49)
            UP -> "front" to Vector2(position.x, 49)
            else -> error("")
        }
        return State(newPosition, newSide, 3)
    }
    if (facing == RIGHT) {
        val newSide = when (side) {
            "front" -> "right"
            "right" -> "back"
            "back" -> "left"
            "left" -> "front"
            else -> error("!!")
        }
        val newPosition = Vector2(0, position.y)
        return State(newPosition, newSide, 0)
    }
    if (facing == LEFT) {
        val newSide = when (side) {
            "front" -> "left"
            "left" -> "back"
            "back" -> "right"
            "right" -> "front"
            else -> error("!!")
        }
        val newPosition = Vector2(49, position.y)
        return State(newPosition, newSide, 2)
    }
    if (facing == DOWN) {
        val (newFacing, newPosition) = when (side) {
            "left" -> RIGHT to Vector2(0, position.x)
            "front" -> DOWN to Vector2(position.x, 0)
            "right" -> LEFT to Vector2(49, position.x)
            "back" -> UP to Vector2(position.x, 49)
            else -> error("")
        }
        return State(newPosition, "bottom", newFacing)
    }
    if (facing == UP) {
        val (newFacing, newPosition) = when (side) {
            "left" -> RIGHT to Vector2(0, position.x)
            "back" -> DOWN to Vector2(position.x, 0)
            "right" -> LEFT to Vector2(49, position.x)
            "front" -> UP to Vector2(position.x, 49)
            else -> error("")
        }
        return State(newPosition, "top", newFacing)
    }
    error("undefined case $position $side $facing")
}

private fun nextTile(state: State): State {
    val direction = when (state.facing) {
        RIGHT -> Vector2(1, 0)
        DOWN -> Vector2(0, 1)
        LEFT -> Vector2(-1, 0)
        UP -> Vector2(0, -1)
        else -> error("!!")
    }
    val (x, y) = state.position + direction
    if (x in 0 until 50 && y in 0 until 50) {
        return State(Vector2(x, y), state.side, state.facing)
    }
    return traverseSides(state)
}

private fun turn(facing: Int, instruction: String): Int {
    val k = when (instruction) {
        "L" -> -1
        "R" -> 1
        else -> error("!!")
    }
    return Math.floorMod(facing + k, 4)
}

private tailrec fun step(state: State, world: CubeWorld, n: Int): State {
    if (n == 0) {
        return state
    }
    val next = nextTile(state)
    return if (world[next.side]!![next.position] == '#') {
        state
    } else {
        step(next, world, n - 1)
    }
}

private fun executeInstructions(initial: State, instructions: List<String>, world: CubeWorld): State {
    var state = initial
    for (instruction in instructions) {
        state = when (instruction) {
            "L", "R" -> State(state.position, state.side, turn(state.facing, instruction))
            else -> step(state, world, instruction.toInt())
        }
    }
    return state
}


fun main() {
    val sample = File("input/day22_sample.txt")
    val input = File("input/day22.txt")

    val reader = input.bufferedReader()

    val board = reader.lines()
        .takeWhile { line -> line.isNotEmpty() }
        .map { it.toList().toTypedArray() }
        .toList().toTypedArray()

    val path = reader.readLine()
        .replace("L", ",L,")
        .replace("R", ",R,")
        .split(",")

    val dirs = ">v<^"

    val start = Vector2(board.first().indexOfFirst { it == '.' }, 0)

    board[start] = 'S'

    println(board.joinToString("\n") { it.joinToString("") })
    println(start)

    fun nextTile(position: Vector2, facing: Int): Vector2? {
        val direction = when (facing) {
            RIGHT -> Vector2(1, 0)
            DOWN -> Vector2(0, 1)
            LEFT -> Vector2(-1, 0)
            UP -> Vector2(0, -1)
            else -> error("!!")
        }
        val next = position + direction
        return when (board.getOrNull(next) ?: ' ') {
            '.', 'S', '>', 'v', '<', '^' -> next
            '#' -> null
            else -> {
                var temp = position
                while ((board.getOrNull(temp - direction) ?: ' ') != ' ') {
                    temp -= direction
                }
                if (board[temp] == '#') null else temp
            }
        }
    }

    fun move(from: Vector2, steps: Int, facing: Int): Vector2 {
        var position = from
        board[position] = dirs[facing]
        for (step in 0 until steps) {
            val next = nextTile(position, facing) ?: return position
            position = next
            board[position] = dirs[facing]
        }
        return position
    }

    var facing = 0
    var position = start

    for (instruction in path) {
        if (instruction == "L" || instruction == "R") {
            facing = turn(facing, instruction)
        } else {
            position = move(position, instruction.toInt(), facing)
        }
    }

    println(board.joinToString("\n") { it.joinToString("") })

    val answer = 1000 * (position.y + 1) + 4 * (position.x + 1) + facing
    println(answer)
}

