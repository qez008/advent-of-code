package adventofcode.solutions;

import com.google.common.base.Joiner;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

@lombok.RequiredArgsConstructor
class Day23 implements Solution {

    private final List<Connection> connections;

    record Connection(String left, String right) {

        @Override
        public String toString() {
            return left + "-" + right;
        }
    }

    HashMap<String, ArrayList<String>> createEdgeMap() {
        var edges = new HashMap<String, ArrayList<String>>();

        BiConsumer<String, String> addEdge = (left, right) -> {
            if (edges.get(left) instanceof ArrayList<String> list) {
                list.add(right);
            } else {
                var list = new ArrayList<String>();
                list.add(right);
                edges.put(left, list);
            }
        };
        for (var con : connections) {
            addEdge.accept(con.left(), con.right());
            addEdge.accept(con.right(), con.left());
        }
        return edges;
    }

    public Integer part1() {
        var edges = createEdgeMap();

        var seen = new HashSet<List<String>>();
        var clumps = new ArrayList<List<String>>();

        for (var entry : edges.entrySet()) {
            var compA = entry.getKey();
            var consA = entry.getValue();
            for (var compB : consA) {
                if (edges.get(compB) instanceof ArrayList<String> consB) {
                    for (var compC : consB) {
                        var key = Stream.of(compA, compB, compC).sorted().toList();
                        if (seen.contains(key)) {
                            continue;
                        }
                        if (consA.contains(compC)) {
                            clumps.add(key);
                        }
                        seen.add(key);
                    }
                }
            }
        }
        return (int) clumps.stream().filter(list -> list.stream().anyMatch(comp -> comp.startsWith("t"))).count();
    }

    public String part2() {
        var edges = createEdgeMap();
        var clumps = new ArrayList<List<String>>();

        for (var entry : edges.entrySet()) {
            var comp = entry.getKey();
            var cons = entry.getValue();
            clumps.add(List.of(comp));
            var newClumps = new ArrayList<List<String>>();
            for (var clump : clumps) {
                if (cons.containsAll(clump)) {
                    var newClump = new ArrayList<>(clump);
                    newClump.add(comp);
                    newClumps.add(newClump);
                }
            }
            clumps.addAll(newClumps);
        }
        return clumps
                .stream()
                .max(Comparator.comparingInt(List::size))
                .map(clump -> {
                    var sorted = clump.stream().sorted().toList();
                    return Joiner.on(",").join(sorted);
                })
                .orElseThrow();
    }
}
