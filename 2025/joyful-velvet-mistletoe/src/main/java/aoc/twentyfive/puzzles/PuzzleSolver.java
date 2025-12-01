package aoc.twentyfive.puzzles;

import aoc.twentyfive.comon.InputUtil;

import static java.lang.IO.println;

abstract class PuzzleSolver<Input> {

    public abstract Input parseInput();
    public abstract int part1(Input input);
    public abstract int part2(Input input);

    void main() {
        var input = parseInput();
        println(part1(input));
        println(part2(input));
    }
}
