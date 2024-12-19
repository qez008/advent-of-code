package adventofcode.solutions;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
class Day19 implements Solution {

    private final List<String> patterns;
    private final List<String> designs;

    public Long part1() {
        return designs.stream().filter(this::isPossible).count();
    }

    public Long part2() {
        return designs.stream().map(x -> combinations(x, new State("", x), new HashMap<>())).reduce(0L, Long::sum);
    }

    record State(String acc, String rem) {}

    boolean isPossible(String design) {
        var queue = new LinkedList<State>();
        queue.add(new State("", design));
        var visited = new HashSet<String>();

        while (queue.poll() instanceof State(String acc, String rem)) {
            if (visited.contains(acc)) {
                continue;
            }
            if (rem.isEmpty()) {
                return acc.equals(design);
            }
            for (var p : patterns) {
                if (rem.startsWith(p)) {
                    queue.add(new State(acc + p, rem.substring(p.length())));
                }
            }
            visited.add(acc);
        }
        return false;
    }

    long combinations(String design, State state, HashMap<String, Long> mem) {
        if (mem.get(state.acc) instanceof Long cached) {
            return cached;
        }
        if (state.rem.isEmpty()) {
            if (state.acc.equals(design)) {
                return 1L;
            }
            return 0L;
        }

        var value = 0L;
        for (var p : patterns) {
            if (state.rem.startsWith(p)) {
                var next = new State(state.acc + p, state.rem.substring(p.length()));
                value += combinations(design, next, mem);
            }
        }
        mem.put(state.acc, value);
        return value;
    }
}
