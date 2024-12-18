package adventofcode.solutions;

import adventofcode.util.IntVector2;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class Day13 implements Solution {

    record Button(IntVector2 dir, int cost) {}

    record Machine(Button buttonA, Button buttonB, IntVector2 prize) {}

    private final List<Machine> machines;

    public Long part1() {
        return machines
                .stream()
                .map(x -> solve(x, 0L))
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .reduce(0L, Long::sum);
    }

    public Long part2() {
        return machines
                .stream()
                .map(x -> solve(x, 10000000000000L))
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .reduce(0L, Long::sum);
    }

    private Optional<Long> solve(Machine machine, long offset) {
        long ax = machine.buttonA.dir.x();
        long ay = machine.buttonA.dir.y();

        long bx = machine.buttonB.dir.x();
        long by = machine.buttonB.dir.y();

        long px = machine.prize.x() + offset;
        long py = machine.prize.y() + offset;

        long d = ax * by - ay * bx;
        long di = px * by - py * bx;
        long dj = py * ax - px * ay;

        if (di % d == 0 && dj % d == 0) {
            return Optional.of((long) 3 * di / d + dj / d);
        }
        return Optional.empty();
    }

}
