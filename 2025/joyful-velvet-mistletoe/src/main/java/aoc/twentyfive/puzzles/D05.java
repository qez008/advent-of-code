package aoc.twentyfive.puzzles;

import aoc.twentyfive.common.InputUtil;
import aoc.twentyfive.common.Longerval;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class D05 extends PuzzleSolver<D05.PuzzleInput> {

    public record PuzzleInput(Set<Longerval> intervals, Set<Long> ids) {}

    @Override public PuzzleInput parseInput() {
        var data = InputUtil.getInput(5);
        var split = data.indexOf("");
        var intervals = data
                .subList(0, split)
                .stream()
                .map(Longerval::from)
                .toList();
        var ids = data
                .subList(split + 1, data.size())
                .stream().map(Long::parseLong)
                .toList();
        return new PuzzleInput(new HashSet<>(intervals), new HashSet<>(ids));
    }

    @Override public long part1(PuzzleInput input) {
        return input
                .ids()
                .stream()
                .filter(id -> input.intervals().stream().anyMatch(interval -> interval.contains(id)))
                .count();
    }

    @Override public long part2(PuzzleInput input) {
        var ranges = input.intervals().stream().toList();
        while (true) {
            var joinedIntervals = joinIntervals(ranges);
            var sizeDiff = ranges.size() - joinedIntervals.size();
            ranges = joinedIntervals;
            if (sizeDiff == 0) {
                break;
            }
        }
        return ranges.stream().map(Longerval::size).reduce(0L, Long::sum);
    }

    private static ArrayList<Longerval> joinIntervals(List<Longerval> intervals) {
        var joinedIntervals = new ArrayList<Longerval>();
        for (var newInterval : intervals) {
            var handled = false;
            for (var i = 0; i < joinedIntervals.size(); i++) {
                var interval = joinedIntervals.get(i);
                if (interval.isOverlapping(newInterval)) {
                    joinedIntervals.set(i, Longerval.combine(interval, newInterval));
                    handled = true;
                    break;
                }
            }
            if (!handled) {
                joinedIntervals.add(newInterval);
            }
        }
        return joinedIntervals;
    }
}
