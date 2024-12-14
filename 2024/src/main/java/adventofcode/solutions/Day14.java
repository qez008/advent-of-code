package adventofcode.solutions;

import adventofcode.util.IntVector2;
import com.google.common.base.Joiner;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class Day14 implements Solution {

    private static final IntVector2 sampleSize = new IntVector2(11, 7);
    private static final IntVector2 realSize = new IntVector2(101, 103);

    record Robot(IntVector2 start, IntVector2 velocity) {}

    private final List<Robot> robots;

    public long part1() {
        var size = realSize;
        var finalPositions = robots.stream().map(robot -> simulate(robot, size));
        var quadrants = finalPositions
                .filter(pos -> pos.x() != size.x() / 2 && pos.y() != size.y() / 2)
                .collect(Collectors.groupingBy(x -> findQuadrant(x, size)));
        return quadrants
                .values()
                .stream()
                .map(xs -> (long) xs.size())
                .peek(System.out::println)
                .reduce(1L, (a, b) -> a * b);
    }

    private IntVector2 simulate(Robot robot, IntVector2 size) {
        var position = robot.start().plus(robot.velocity().times(100));
        BiFunction<Integer, Integer, Integer> mod = (a, b) -> (a % b + b) % b;
        return new IntVector2(
                mod.apply(position.x(), size.x()),
                mod.apply(position.y(), size.y())
        );
    }

    private IntVector2 findQuadrant(IntVector2 position, IntVector2 size) {
        var a = position.x() < size.x() / 2 ? 0 : 1;
        var b = position.y() < size.y() / 2 ? 0 : 1;
        return new IntVector2(a, b);
    }

    public long part2() {
        var states = io.vavr.collection.List
                .ofAll(robots)
                .zip(robots.stream().map(Robot::start).toList());

        final var floor = new Character[realSize.y()][realSize.x()];

        for (var i = 0; i < 10000; i++) {
            for (var x = 0; x < realSize.x(); x++) {
                for (var y = 0; y < realSize.y(); y++) {
                    floor[y][x] = '.';
                }
            }
            states = states.map(state -> state.map((robot, position) -> {
                var m = position.plus(robot.velocity());
                BiFunction<Integer, Integer, Integer> mod = (a, b) -> (a % b + b) % b;
                var n = new IntVector2(
                        mod.apply(m.x(), realSize.x()),
                        mod.apply(m.y(), realSize.y())
                );
                floor[n.y()][n.x()] = 'X';
                return new Tuple2<>(robot, n);
            }));

            for (var y = 0; y < realSize.y(); y++) {

                if (Joiner.on("").join(Arrays.asList(floor[y])).contains("XXXXXXX")) {
                    System.out.println(i);
                    for (var j = 0; j < realSize.y(); j++) {
                        System.out.println(Joiner.on("").join(floor[j]));
                    }
                    System.out.println();
                }
            }
        }

        return 0;
    }
}
