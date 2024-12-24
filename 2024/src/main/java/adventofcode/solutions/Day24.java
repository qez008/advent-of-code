package adventofcode.solutions;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@lombok.RequiredArgsConstructor
class Day24 implements Solution {

    private final HashMap<String, Boolean> initialData;
    private final List<Gate> gates;

    record Gate(String left, String op, String right, String result) {}

    public Long part1() {
        var data = solve(Map.of());
        return convertNumber(data, Map.of(), s -> s.startsWith("z"));
    }

    HashMap<String, Boolean> solve(Map<String, String> swapped) {
        var data = new HashMap<>(initialData);
        var queue = new LinkedList<>(gates);

        var i = 0;
        while (queue.poll() instanceof Gate gate) {
            if (i++ > gates.size() * 100) {
                throw new RuntimeException("stuck");
            }

            if (!data.containsKey(getAlias(gate.left, swapped)) || !data.containsKey(getAlias(gate.right, swapped))) {
                queue.add(gate);
                continue;
            }
            var left = data.get(getAlias(gate.left, swapped));
            var right = data.get(getAlias(gate.right, swapped));
            var result = switch (gate.op) {
                case "AND" -> left && right;
                case "OR" -> left || right;
                case "XOR" -> left ^ right;
                default -> throw new IllegalStateException("Unexpected value: " + gate.op);
            };
            data.put(getAlias(gate.result, swapped), result);
        }
        return data;
    }

    String getAlias(String name, Map<String, String> swappedGates) {
        return swappedGates.getOrDefault(name, name);
    }

    public String part2() {
        var gateNames = gates
                .stream()
                .flatMap(gate -> Stream.of(gate.left, gate.right, gate.result))
                .collect(Collectors.toSet());

        for (var gateA : gateNames) {
            for (var gateB : gateNames) {
                for (var gateC : gateNames) {
                    for (var gateD : gateNames) {
                        if (Stream.of(gateA, gateB, gateC, gateD).distinct().count() < 4) {
                            continue;
                        }
                        var swapped = new HashMap<String, String>();
                        swapped.put(gateA, gateB);
                        swapped.put(gateB, gateA);
                        swapped.put(gateC, gateD);
                        swapped.put(gateD, gateC);

                        //                        System.out.println(swapped);

                        try {
                            var data = solve(swapped);
                            var x = convertNumber(data, swapped, s -> s.startsWith("x"));
                            var y = convertNumber(data, swapped, s -> s.startsWith("y"));
                            var z = convertNumber(data, swapped, s -> s.startsWith("z"));
                            System.out.println(swapped.values().stream().sorted().toList());
                            System.out.println(x + "+" + y + "=" + z);
                            if (x + y == z) {
                                return Stream
                                        .of(gateA, gateB, gateC, gateD)
                                        .sorted()
                                        .collect(Collectors.joining(","));
                            }
                        } catch (RuntimeException e) {
                            continue;
                        }
                    }
                }
            }
        }
        return null;
    }

    long convertNumber(
            Map<String, Boolean> data,
            Map<String, String> swapped,
            Predicate<String> predicate
    ) {
        var bits = data
                .entrySet()
                .stream()
                .filter(e -> predicate.test(swapped.getOrDefault(e.getKey(), e.getKey())))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
        var result = 0L;
        for (var b : bits.reversed()) {
            result = (result << 1) | (b ? 1 : 0);
        }
        return result;
    }
}
