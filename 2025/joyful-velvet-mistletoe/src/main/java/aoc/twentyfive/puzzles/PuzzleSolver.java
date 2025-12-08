package aoc.twentyfive.puzzles;

import static java.lang.IO.println;

abstract class PuzzleSolver<Input> {

    public abstract Input parseInput();
    public abstract long part1(Input input);
    public abstract long part2(Input input);

    void main() {
        var input = parseInput();
        println("p1=" + part1(input));
        println("p2=" + part2(input));
    }
}
