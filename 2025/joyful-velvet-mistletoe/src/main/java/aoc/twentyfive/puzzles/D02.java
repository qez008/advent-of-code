package aoc.twentyfive.puzzles;

import aoc.twentyfive.common.InputUtil;

import java.util.Arrays;
import java.util.List;

class D02 extends PuzzleSolver<List<String>> {

    record Range(long start, long end) {

        public static Range fromString(String str) {
            var split = str.split("-");
            return new Range(Long.parseLong(split[0]), Long.parseLong(split[1]));
        }
    }

    @Override public List<String> parseInput() {
        return InputUtil
                .getInput(2)
                .stream()
                .map(x -> x.split(","))
                .flatMap(Arrays::stream)
                .toList();
    }

    @Override public long part1(List<String> strings) {
        return strings
                .stream()
                .map(Range::fromString)
                .map(x -> {
                    var count = 0L;
                    for (var l = x.start; l <= x.end; l++) {
                        var str = String.valueOf(l);
                        if (str.length() % 2 == 0) {
                            var middle = str.length() / 2;
                            var a = str.substring(0, middle);
                            var b = str.substring(middle);
                            if (a.equals(b)) {
                                count += l;
                            }
                        }
                    }
                    return count;
                })
                .reduce(0L, Long::sum);
    }

    @Override public long part2(List<String> strings) {
        return strings
                .stream()
                .map(Range::fromString)
                .map(x -> {
                    var count = 0L;
                    for (var current = x.start; current <= x.end; current++) {
                        var str = String.valueOf(current);
                        for (var i = 1; i <= str.length() / 2; i++) {
                            if (str.length() % i != 0) {
                                continue;
                            }
                            var pattern = str.substring(0, i);
                            var isRepeatingPattern = true;
                            for (var j = 0; j < str.length(); j += i) {
                                if (!str.substring(j, j + i).equals(pattern)) {
                                    isRepeatingPattern = false;
                                    break;
                                }
                            }
                            if (isRepeatingPattern) {
                                count += current;
                                break;
                            }
                        }
                    }
                    return count;
                })
                .reduce(0L, Long::sum);
    }
}
