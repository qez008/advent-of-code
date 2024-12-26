package adventofcode.solutions;

import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@lombok.RequiredArgsConstructor
class Day24 implements Solution {

    private static final List<Character> XY = List.of('x', 'y');

    private final HashMap<String, Boolean> initialData;
    private final List<Gate> gates;

    record Gate(String left, String op, String right, String out) {}

    @Override
    public Long part1() {
        var data = new HashMap<>(initialData);
        var queue = new LinkedList<>(gates);

        while (queue.poll() instanceof Gate gate) {
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
        var bits = data
                .entrySet()
                .stream()
                .filter(e -> e.getKey().charAt(0) == 'z')
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();
        var ans = 0L;
        for (var b : bits.reversed()) {
            ans = (ans << 1) | (b ? 1 : 0);
        }
        return ans;
    }

    @Override
    public String part2() {
        var pred = rule1().or(rule2()).or(rule3(gates)).or(rule4(gates));
        var badWires = gates.stream().filter(pred).map(Gate::out).distinct().sorted().toList();
        return Joiner.on(",").join(badWires);
    }

    Predicate<Gate> rule1() {
        return gate -> !gate.op.equals("XOR")
                       && gate.out.charAt(0) == 'z'
                       && !gate.out.equals("z45");
    }

    Predicate<Gate> rule2() {
        return gate -> gate.op.equals("XOR")
                       && !XY.contains(gate.left.charAt(0))
                       && !XY.contains(gate.right.charAt(0))
                       && gate.out.charAt(0) != 'z';
    }

    Predicate<Gate> rule3(List<Gate> gates) {
        var xorIns = gates
                .stream()
                .filter(g -> g.op.equals("XOR"))
                .flatMap(g -> Stream.of(g.left, g.right))
                .toList();
        return gate -> gate.op.equals("XOR")
                       && !(gate.left.endsWith("00") || gate.right.endsWith("00"))
                       && XY.contains(gate.left.charAt(0))
                       && XY.contains(gate.right.charAt(0))
                       && xorIns.stream().noneMatch(gate.out::equals);
    }

    Predicate<Gate> rule4(List<Gate> gates) {
        var orIns = gates
                .stream()
                .filter(g -> g.op.equals("OR"))
                .flatMap(g -> Stream.of(g.left, g.right))
                .toList();
        return gate -> gate.op.equals("AND")
                       && !(gate.left.endsWith("00") || gate.right.endsWith("00"))
                       && orIns.stream().noneMatch(gate.out::equals);
    }
}
