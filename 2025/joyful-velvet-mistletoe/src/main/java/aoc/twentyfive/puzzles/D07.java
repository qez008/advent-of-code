package aoc.twentyfive.puzzles;

import aoc.twentyfive.common.Grid;
import aoc.twentyfive.common.InputUtil;
import aoc.twentyfive.common.Vec2;
import io.vavr.control.Try;

import java.util.*;

public class D07 extends PuzzleSolver<Grid<Character>> {

    @Override public Grid<Character> parseInput() {
        var xs = InputUtil
                .getInput(7)
                .stream()
                .map(x -> x.chars().mapToObj(y -> (char) y).toList())
                .map(ArrayList::new)
                .toList();
        return new Grid<>(xs);
    }

    @Override public long part1(Grid<Character> grid) {
        var queue = new ArrayDeque<Vec2<Integer>>();
        queue.add(Objects.requireNonNull(grid.posOfFirst('S')));
        var splits = 0L;

        while (!queue.isEmpty()) {
            var pos = queue.pop();
            if (pos.y() >= grid.h() || grid.get(pos) == '|') {
                continue;
            }
            if (grid.get(pos) == '^') {
                var left = new Vec2<>(pos.x() - 1, pos.y());
                var addLeft = Try
                        .of(() -> grid.get(left) == '.')
                        .getOrElse(false);
                if (addLeft) {
                    queue.add(left);
                }
                var right = new Vec2<>(pos.x() + 1, pos.y());
                var addRight = Try
                        .of(() -> grid.get(right) == '.')
                        .getOrElse(false);
                if (addRight) {
                    queue.add(right);
                }
                splits++;
            } else {
                queue.add(new Vec2<>(pos.x(), pos.y() + 1));
                if (grid.get(pos) != 'S') {
                    grid.set(pos, '|');
                }
            }
        }

        return splits;
    }

    // 32157 too low
    // 5921061943075 ans
    @Override public long part2(Grid<Character> grid) {
        return rec(grid.posOfFirst('S'), grid, new HashMap<>());
    }

    private long rec(Vec2<Integer> pos, Grid<Character> grid, HashMap<Vec2<Integer>, Long> mem) {
        if (mem.containsKey(pos)) {
            return mem.get(pos);
        }
        if (pos.y() >= grid.h()) {
            return 1L;
        }
        return switch (Try.of(() -> grid.get(pos)).getOrElse('X')) {
            case 'S', '.', '|' -> rec(new Vec2<>(pos.x(), pos.y() + 1), grid, mem);
            case '^' -> {
                var left = rec(new Vec2<>(pos.x() - 1, pos.y()), grid, mem);
                var right = rec(new Vec2<>(pos.x() + 1, pos.y()), grid, mem);
                mem.put(pos, left + right);
                yield left + right;
            }
            default -> throw new IllegalStateException();
        };
    }

}
