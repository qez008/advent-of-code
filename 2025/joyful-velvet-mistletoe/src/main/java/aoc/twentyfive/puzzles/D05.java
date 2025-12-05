package aoc.twentyfive.puzzles;

import aoc.twentyfive.comon.InputUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class D05 extends PuzzleSolver<D05.PuzzleInput> {

    private record Range(Long start, Long end) implements Comparable<Range> {

        public static Range from(String str) {
            var split = str.split("-");
            return new Range(Long.parseLong(split[0]), Long.parseLong(split[1]));
        }

        public boolean contains(long value) {
            return value >= start && value <= end;
        }

        @Override public int compareTo(@NotNull Range o) {
            return this.start.compareTo(o.start);
        }

        public long size() {
            return end - start + 1;
        }

        @Override public @NotNull String toString() {
            return "Range[start=%d, end=%d, size=%d]".formatted(start, end, size());
        }
    }

    public record PuzzleInput(Set<Range> ranges, Set<Long> ids) {}

    @Override public PuzzleInput parseInput() {
        var data = InputUtil.getInput(5);
        var split = data.indexOf("");
        var ranges = data
                .subList(0, split)
                .stream()
                .map(Range::from)
                .toList();
        var ids = data
                .subList(split + 1, data.size())
                .stream().map(Long::parseLong)
                .toList();
        return new PuzzleInput(new HashSet<>(ranges), new HashSet<>(ids));
    }

    @Override public long part1(PuzzleInput input) {
        return input
                .ids()
                .stream()
                .filter(id -> input.ranges().stream().anyMatch(range -> range.contains(id)))
                .count();
    }

    // too high 346747903003528
    // too high 346747903003518
    // ans      344260049617193
    @Override public long part2(PuzzleInput input) {
        var ranges = input.ranges().stream().toList();
        while (true) {
            var joinedRanges = joinRanges(ranges).stream().sorted().toList();
            if (joinedRanges.size() == ranges.size()) {
                ranges = joinedRanges;
                break;
            }
            ranges = joinedRanges;
        }
        for (var range : ranges.stream().sorted().toList()) {
            IO.println(range);
        }
        return ranges
                .stream()
                .map(x -> x.end - x.start + 1)
                .reduce(0L, Long::sum);
    }

    private static ArrayList<Range> joinRanges(List<Range> ranges) {
        var joinedRanges = new ArrayList<Range>();
        for (var newRange : ranges) {
            var handled = false;
            for (var i = 0; i < joinedRanges.size(); i++) {
                var range = joinedRanges.get(i);
                // existing range contains whole new range
                if (range.contains(newRange.start) && range.contains(newRange.end)) {
                    handled = true;
                    break;
                }
                // exiting range overlaps new range
                if (range.contains(newRange.start) || range.contains(newRange.end)) {
                    var joinedRange = new Range(
                            Math.min(range.start, newRange.start),
                            Math.max(range.end, newRange.end)
                    );
                    joinedRanges.set(i, joinedRange);
                    handled = true;
                    break;
                }
            }
            if (!handled) {
                joinedRanges.add(newRange);
            }
        }
        return joinedRanges;
    }
}
