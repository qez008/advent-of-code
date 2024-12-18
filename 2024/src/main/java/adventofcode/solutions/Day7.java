package adventofcode.solutions;

import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class Day7 implements Solution {

    private final List<Tuple2<Long, List<Long>>> equations;

    static Tuple2<Long, List<Long>> findEquation(String equation) {
        var split = equation.split(": ");
        var value = Long.parseLong(split[0]);
        var numbers = Arrays.stream(split[1].split(" ")).map(Long::parseLong).toList();
        return new Tuple2<>(value, numbers);
    }

    public Long part1() {
        List<BiFunction<Long, Long, Long>> operators = List.of(
                (a, b) -> a + b,
                (a, b) -> a * b
        );
        return solve(equations, operators);
    }

    public Long part2() {
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
