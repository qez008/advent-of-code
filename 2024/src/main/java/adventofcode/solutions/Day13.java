package adventofcode.solutions;

import adventofcode.util.IntVector2;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Day13 implements Solution {

    record Button(IntVector2 dir, int cost) {}

    record Machine(Button buttonA, Button buttonB, IntVector2 prize) {}

    private final List<Machine> machines;

    public long part1() {
        return machines.stream().map(this::solve).reduce(0L, Long::sum);
    }

    public long part2() {
        return 0;
    }

    private long solve(Machine machine) {
        System.out.println(machine);
        var mem = new HashMap<MemEntry, Optional<Long>>();
        var start = new IntVector2(0, 0);
        return rec(100, start, machine, mem).orElse(0L);
    }

    record MemEntry(IntVector2 position, int i) {}

    private Optional<Long> rec(int i, IntVector2 position, Machine machine, HashMap<MemEntry, Optional<Long>> mem) {
        if (position.equals(machine.prize)) {
            return Optional.of(0L);
        }
        if (i == 0 || position.x() > machine.prize.x() || position.y() > machine.prize.y()) {
            return Optional.empty();
        }

        var key = new MemEntry(position, i);

        if (mem.get(key) instanceof Optional<Long> value) {
            return value;
        }

        var a = rec(i - 1, position.plus(machine.buttonA.dir), machine, mem).map(x -> x + machine.buttonA.cost);
        var b = rec(i - 1, position.plus(machine.buttonB.dir), machine, mem).map(x -> x + machine.buttonB.cost);

        var value = Stream.of(a, b).filter(Optional::isPresent).map(Optional::get).min(Long::compareTo);
        mem.put(key, value);
        return value;
    }
}
