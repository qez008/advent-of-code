package aoc.twentyfive.puzzles;

import aoc.twentyfive.common.InputUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class D06 extends PuzzleSolver<List<String>> {

    @Override public List<String> parseInput() {
        return InputUtil.getInput(6);
    }

    @Override public long part1(List<String> input) {
        var splitInput = input
                .stream()
                .map(x -> x.replaceAll("\\s+", " ").trim().split(" "))
                .toList();
        var numbers = IntStream
                .range(0, splitInput.getFirst().length)
                .mapToObj(i -> splitInput
                        .subList(0, splitInput.size() - 1)
                        .stream()
                        .map(x -> Long.parseLong(x[i]))
                        .toList()
                )
                .toList();
        var ops = splitInput.getLast();
        return solve(numbers, ops);
    }

    @Override public long part2(List<String> input) {
        var numbers = input.subList(0, input.size() - 1);
        var transposed = new ArrayList<ArrayList<Long>>();
        transposed.add(new ArrayList<>());

        for (var i = 0; i < numbers.getFirst().length(); i++) {
            final var finalI = i;
            String num = numbers
                    .stream()
                    .map(x -> String.valueOf(x.charAt(finalI)))
                    .collect(Collectors.joining());
            if (num.isBlank()) {
                transposed.add(new ArrayList<>());
            } else {
                transposed.getLast().add(Long.parseLong(num.trim()));
            }
        }
        var ops = input.getLast().split("\\s+");
        return solve(transposed, ops);
    }

    private static long solve(List<? extends List<Long>> numbers, String[] ops) {
        return IntStream
                .range(0, ops.length)
                .mapToLong(i -> Objects.equals(ops[i], "+")
                        ? numbers.get(i).stream().reduce(0L, Long::sum)
                        : numbers.get(i).stream().reduce(1L, (a, b) -> a * b))
                .reduce(0L, Long::sum);
    }
}
