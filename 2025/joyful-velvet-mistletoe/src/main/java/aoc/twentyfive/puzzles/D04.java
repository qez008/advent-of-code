package aoc.twentyfive.puzzles;

import aoc.twentyfive.comon.GridUtil;
import aoc.twentyfive.comon.Index2D;
import aoc.twentyfive.comon.InputUtil;
import io.vavr.control.Try;

import java.util.List;

class D04 extends PuzzleSolver<List<String>> {

    @Override public List<String> parseInput() {
        return InputUtil.getInput(4);
    }

    @Override public long part1(List<String> rows) {
        return GridUtil
                .indices2D(rows.getFirst().length(), rows.size())
                .stream()
                .filter(a -> isPaperRoll(a, rows))
                .filter(a -> isAccessible(rows, a))
                .count();
    }

    @Override public long part2(List<String> rows) {
        var count = 0L;
        while (true) {
            long canAccessCount = GridUtil
                    .indices2D(rows.getFirst().length(), rows.size())
                    .stream()
                    .filter(a -> isPaperRoll(a, rows))
                    .filter(a -> isAccessible(rows, a))
                    .peek(a -> {
                        var str = rows.get(a.y());
                        var builder = new StringBuilder(str);
                        builder.setCharAt(a.x(), '.');
                        rows.set(a.y(), builder.toString());
                    })
                    .count();
            if (canAccessCount == 0) {
                break;
            }
            count += canAccessCount;
        }
        return count;
    }

    private static boolean isAccessible(List<String> rows, Index2D a) {
        var adjacentRolls = GridUtil
                .neighborsOf(a)
                .stream()
                .filter(b -> Try.of(() -> isPaperRoll(b, rows)).getOrElse(false))
                .count();
        return adjacentRolls < 4;
    }

    private static boolean isPaperRoll(Index2D index, List<String> rows) {
        return rows.get(index.y()).charAt(index.x()) == '@';
    }
}
