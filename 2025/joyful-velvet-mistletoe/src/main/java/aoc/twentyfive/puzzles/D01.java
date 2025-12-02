package aoc.twentyfive.puzzles;

import aoc.twentyfive.comon.InputUtil;

import java.util.List;

import static aoc.twentyfive.comon.MathUtil.posMod;
import static java.lang.Math.abs;

class D01 extends PuzzleSolver<List<String>> {

    @Override public List<String> parseInput() {
        return InputUtil.getInput(1);
    }

    @Override public long part1(List<String> input) {
        var pointer = 50;
        var zeroCount = 0;

        for (var line : input) {
            var num = Integer.parseInt(line.substring(1));
            if (line.charAt(0) == 'L') {
                pointer -= num;
            } else {
                pointer += num;
            }
            pointer = posMod(pointer, 100);
            if (pointer == 0) {
                zeroCount++;
            }
        }
        return zeroCount;
    }

    @Override public long part2(List<String> input) {
        var pointer = 50;
        var zeroClicks = 0;

        for (var line : input) {
            var num = Integer.parseInt(line.substring(1));
            var next = line.charAt(0) == 'L' ? pointer - num : pointer + num;

            if (next == 0) {
                zeroClicks++;
            } else if (next < 0) {
                var y = pointer == 0 ? 0 : 1; // if already on zero, count one less
                zeroClicks += y + abs(next) / 100;
            } else if (next >= 100) {
                zeroClicks += next / 100;
            }
            pointer = posMod(next, 100);
        }
        return zeroClicks;
    }
}
