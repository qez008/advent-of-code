package adeventofcode.solutions;

import io.vavr.Tuple2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiFunction;

public class Day7 {

    private final List<Tuple2<Long, List<Long>>> equations;

    public Day7(String input) {
        try (var lines = Files.lines(Path.of(input))) {
            this.equations = lines.parallel().map(Day7::findEquation).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Tuple2<Long, List<Long>> findEquation(String equation) {
        var split = equation.split(": ");
        var value = Long.parseLong(split[0]);
        var numbers = Arrays.stream(split[1].split(" ")).map(Long::parseLong).toList();
        return new Tuple2<>(value, numbers);
    }

    long one() {
        List<BiFunction<Long, Long, Long>> operators = List.of(
                (a, b) -> a + b,
                (a, b) -> a * b
        );
        return solve(equations, operators);
    }

    long two() {
        List<BiFunction<Long, Long, Long>> operators = List.of(
                (a, b) -> a + b,
                (a, b) -> a * b,
                (a, b) -> Long.parseLong(a + "" + b)
        );
        return solve(equations, operators);
    }

    long solve(
            List<Tuple2<Long, List<Long>>> equations,
            List<BiFunction<Long, Long, Long>> ops
    ) {
        return equations
                .stream()
                .filter(pair -> pair.apply((testValue, numbers) -> numbers
                        .subList(1, numbers.size())
                        .stream()
                        .map(List::of)
                        .reduce(List.of(numbers.getFirst()), (acc, number) -> acc
                                .stream()
                                .flatMap(value -> ops
                                        .stream()
                                        .map(op -> op.apply(value, number.getFirst())))
                                .toList())
                        .contains(testValue)))
                .map(Tuple2::_1)
                .reduce(0L, Long::sum);
    }
}
