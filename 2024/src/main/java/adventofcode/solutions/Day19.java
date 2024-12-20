package adventofcode.solutions;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
class Day19 implements Solution {

    private final List<String> patterns;
    private final List<String> designs;

    public Long part1() {
        return designs
                .stream()
                .filter(x -> numCombos(new State("", x), new HashMap<>()) > 0L)
                .count();
    }

    public Long part2() {
        return designs
                .stream()
                .map(x -> numCombos(new State("", x), new HashMap<>()))
                .reduce(0L, Long::sum);
    }

    record State(String acc, String rem) {}

    long numCombos(State state, HashMap<String, Long> mem) {
        if (mem.get(state.acc) instanceof Long cached) {
            return cached;
        }
        if (state.rem.isEmpty()) {
            return 1L;
        }
        var value = 0L;
        for (var p : patterns) {
            if (state.rem.startsWith(p)) {
                var next = new State(state.acc + p, state.rem.substring(p.length()));
                value += numCombos(next, mem);
            }
        }
        mem.put(state.acc, value);
        return value;
    }
}
