package adventofcode.solutions;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@lombok.RequiredArgsConstructor
class Day24 implements Solution {

    private final HashMap<String, Boolean> initialData;
    private final List<Gate> gates;

    @AllArgsConstructor
    static class Gate {
        String left;
        String op;
        String right;
        String out;

        @Override
        public String toString() {
            return left + " " + op + " " + right + " -> " + out;
        }
    }

    public Long part1() {
        var data = solve(gates);
        return convertNumber(data, s -> s.startsWith("z"));
    }

    HashMap<String, Boolean> solve(List<Gate> gates) {
        var data = new HashMap<>(initialData);
        var queue = new LinkedList<>(gates);

        var i = 0;
        while (queue.poll() instanceof Gate gate) {
            if (i++ > gates.size() * 100) {
                throw new RuntimeException("stuck");
            }

            if (!data.containsKey(gate.left) || !data.containsKey((gate.right))) {
                queue.add(gate);
                continue;
            }
            var left = data.get(gate.left);
            var right = data.get(gate.right);
            var result = switch (gate.op) {
                case "AND" -> left && right;
                case "OR" -> left || right;
                case "XOR" -> left ^ right;
                default -> throw new IllegalStateException("Unexpected value: " + gate.op);
            };
            data.put(gate.out, result);
        }
        return data;
    }

    public String part2() {
        var xy = List.of('x', 'y');

        var a = gates
                .stream()
                .filter(gate -> !gate.op.equals("XOR") && gate.out.charAt(0) == 'z')
                .filter(gate -> !gate.out.equals("z45"))
                .toList();
        var b = gates
                .stream()
                .filter(gate -> gate.op.equals("XOR"))
                .filter(gate -> !xy.contains(gate.left.charAt(0)))
                .filter(gate -> !xy.contains(gate.right.charAt(0)))
                .filter(gate -> gate.out.charAt(0) != 'z')
                .toList();
        var c = gates
                .stream()
                .filter(gate -> gate.op.equals("XOR"))
                .filter(gate -> !(gate.left.endsWith("00") || gate.right.endsWith("00")))
                .filter(gate -> xy.contains(gate.left.charAt(0)))
                .filter(gate -> xy.contains(gate.right.charAt(0)))
                .filter(gate -> gates
                        .stream()
                        .filter(g -> g.op.equals("XOR"))
                        .noneMatch(g -> gate.out.equals(g.left) || gate.out.equals(g.right)))
                .toList();
        var d = gates
                .stream()
                .filter(gate -> gate.op.equals("AND"))
                .filter(gate -> !(gate.left.endsWith("00") || gate.right.endsWith("00")))
                .filter(gate -> gates
                        .stream()
                        .noneMatch(g -> g.op.equals("OR") && (gate.out.equals(g.left) || gate.out.equals(g.right))))
                .toList();

        var sortedOutputs = Stream
                .of(b, a, c, d)
                .flatMap(xs -> xs.stream().map(x -> x.out))
                .distinct()
                .sorted()
                .toList();

        return Joiner.on(",").join(sortedOutputs);
    }

    long convertNumber(Map<String, Boolean> data, Predicate<String> predicate) {
        var bits = data
                .entrySet()
                .stream()
                .filter(e -> predicate.test(e.getKey()))
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
