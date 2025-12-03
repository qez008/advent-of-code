package aoc.twentyfive.puzzles;

import aoc.twentyfive.comon.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class D03 extends PuzzleSolver<List<List<Integer>>> {

    @Override public List<List<Integer>> parseInput() {
        return InputUtil
                .getInput(3)
                .stream()
                .map(bank -> Stream.of(bank.split("")).map(Integer::parseInt).toList())
                .toList();
    }

    @Override public long part1(List<List<Integer>> lists) {
        return lists
                .stream()
                .map(bank -> {
                    var bestSlot1 = findBestBattery(bank, 0, bank.size() - 1);
                    var bestSlot2 = findBestBattery(bank, bestSlot1.index + 1, bank.size());
                    return Long.parseLong("" + bestSlot1.joltage + bestSlot2.joltage);
                })
//                .peek(IO::println)
                .reduce(0L, Long::sum);
    }

    private record Battery(int index, int joltage) {}

    private Battery findBestBattery(List<Integer> bank, int from, int to) {
        Battery best = null;
        for (var i = from; i < to; i++) {
            var battery = new Battery(i, bank.get(i));
            if (best == null || best.joltage < battery.joltage) {
                best = battery;
            }
        }
        return Objects.requireNonNull(best);
    }

    @Override public long part2(List<List<Integer>> lists) {
        return lists
                .stream()
                .map(bank -> {
                    var batteries = new ArrayList<Battery>();
                    batteries.add(new Battery(-1, 0));
                    for (var i = 0; i < 12; i++) {
                        var prevBattery = batteries.get(i);
                        var from = prevBattery.index + 1;
                        var to = bank.size() - (11 - i);
                        batteries.add(findBestBattery(bank, from, to));
                    }

                    return Long.parseLong(
                            batteries.stream().map(x -> String.valueOf(x.joltage)).reduce("", (a, b) -> a + b)
                    );
                })
//                .peek(IO::println)
                .reduce(0L, Long::sum);
    }

    public long sillyPart2(List<List<Integer>> lists) {
        return lists
                .stream()
                .map(bank -> {
                    var batteries = new ArrayList<Battery>();
                    // Start with the initial device (index -1, joltage 0)
                    batteries.add(new Battery(-1, 0));

                    // --- Selection 0 (i=0) ---
                    var prevBattery0 = batteries.getLast();
                    var from0 = prevBattery0.index + 1;
                    var to0 = bank.size() - 11; // Must leave 11 slots
                    batteries.add(findBestBattery(bank, from0, to0));

                    // --- Selection 1 (i=1) ---
                    var prevBattery1 = batteries.getLast();
                    var from1 = prevBattery1.index + 1;
                    var to1 = bank.size() - 10; // Must leave 10 slots
                    batteries.add(findBestBattery(bank, from1, to1));

                    // --- Selection 2 (i=2) ---
                    var prevBattery2 = batteries.getLast();
                    var from2 = prevBattery2.index + 1;
                    var to2 = bank.size() - 9; // Must leave 9 slots
                    batteries.add(findBestBattery(bank, from2, to2));

                    // --- Selection 3 (i=3) ---
                    var prevBattery3 = batteries.getLast();
                    var from3 = prevBattery3.index + 1;
                    var to3 = bank.size() - 8; // Must leave 8 slots
                    batteries.add(findBestBattery(bank, from3, to3));

                    // --- Selection 4 (i=4) ---
                    var prevBattery4 = batteries.getLast();
                    var from4 = prevBattery4.index + 1;
                    var to4 = bank.size() - 7; // Must leave 7 slots
                    batteries.add(findBestBattery(bank, from4, to4));

                    // --- Selection 5 (i=5) ---
                    var prevBattery5 = batteries.getLast();
                    var from5 = prevBattery5.index + 1;
                    var to5 = bank.size() - 6; // Must leave 6 slots
                    batteries.add(findBestBattery(bank, from5, to5));

                    // --- Selection 6 (i=6) ---
                    var prevBattery6 = batteries.getLast();
                    var from6 = prevBattery6.index + 1;
                    var to6 = bank.size() - 5; // Must leave 5 slots
                    batteries.add(findBestBattery(bank, from6, to6));

                    // --- Selection 7 (i=7) ---
                    var prevBattery7 = batteries.getLast();
                    var from7 = prevBattery7.index + 1;
                    var to7 = bank.size() - 4; // Must leave 4 slots
                    batteries.add(findBestBattery(bank, from7, to7));

                    // --- Selection 8 (i=8) ---
                    var prevBattery8 = batteries.getLast();
                    var from8 = prevBattery8.index + 1;
                    var to8 = bank.size() - 3; // Must leave 3 slots
                    batteries.add(findBestBattery(bank, from8, to8));

                    // --- Selection 9 (i=9) ---
                    var prevBattery9 = batteries.getLast();
                    var from9 = prevBattery9.index + 1;
                    var to9 = bank.size() - 2; // Must leave 2 slots
                    batteries.add(findBestBattery(bank, from9, to9));

                    // --- Selection 10 (i=10) ---
                    var prevBattery10 = batteries.getLast();
                    var from10 = prevBattery10.index + 1;
                    var to10 = bank.size() - 1; // Must leave 1 slot
                    batteries.add(findBestBattery(bank, from10, to10));

                    // --- Selection 11 (i=11) ---
                    var prevBattery11 = batteries.getLast();
                    var from11 = prevBattery11.index + 1;
                    var to11 = bank.size(); // Must leave 0 slots
                    batteries.add(findBestBattery(bank, from11, to11));

                    return Long.parseLong(
                            batteries.stream().map(x -> String.valueOf(x.joltage)).reduce("", (a, b) -> a + b)
                    );
                })
                //                .peek(IO::println)
                .reduce(0L, Long::sum);
    }
}