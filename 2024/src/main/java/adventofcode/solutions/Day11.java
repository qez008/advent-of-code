package adventofcode.solutions;

import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
class Day11 implements Solution {

    private final List<Long> initialLine;

    public Integer part1() {
        var line = initialLine;
        for (int i = 0; i < 25; i++) {
            line = line.stream().flatMap(this::transformStone).toList();
            if (i < 7) {
                System.out.println(Joiner.on(" ").join(line));
            }
        }
        return line.size();
    }

    private Stream<Long> transformStone(long stoneSymbol) {
        if (stoneSymbol == 0L) {
            return Stream.of(1L);
        }
        String stringSymbol = Long.toString(stoneSymbol);
        if (stringSymbol.length() % 2 == 0) {
            var a = stringSymbol.substring(0, stringSymbol.length() / 2);
            var b = stringSymbol.substring(stringSymbol.length() / 2);
            return Stream.of(a, b).map(Long::parseLong);
        }

        return Stream.of(stoneSymbol * 2024L);
    }

    public Long part2() {
        var memoization = new HashMap<MemEntry, Long>();
        return initialLine.stream().map(symbol -> stonesAfter(75, symbol, memoization)).reduce(0L, Long::sum);
    }

    record MemEntry(int step, long symbol) {}

    private long stonesAfter(int steps, long symbol, HashMap<MemEntry, Long> mem) {
        if (steps == 0) {
            return 1;
        }

        if (mem.get(new MemEntry(steps, symbol)) instanceof Long value) {
            return value;
        }

        if (symbol == 0L) {
            var value = stonesAfter(steps - 1, 1L, mem);
            mem.put(new MemEntry(steps, symbol), value);
            return value;
        }

        String stringSymbol = Long.toString(symbol);
        if (stringSymbol.length() % 2 == 0) {
            var stringA = stringSymbol.substring(0, stringSymbol.length() / 2);
            var stringB = stringSymbol.substring(stringSymbol.length() / 2);

            var symbolA = Long.parseLong(stringA);
            var symbolB = Long.parseLong(stringB);

            var value = stonesAfter(steps - 1, symbolA, mem) + stonesAfter(steps - 1, symbolB, mem);
            mem.put(new MemEntry(steps, symbol), value);
            return value;
        }

        var value = stonesAfter(steps - 1, symbol * 2024, mem);
        mem.put(new MemEntry(steps, symbol), value);
        return value;
    }
}


