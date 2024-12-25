package adventofcode.solutions;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@lombok.RequiredArgsConstructor
class Day24 implements Solution {

    private final HashMap<String, Boolean> initialData;
    private final List<Gate> gates;

    @AllArgsConstructor
    static class Gate{
        String left;
        String op;
        String right;
        String out;
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
        var nxz = gates
                .stream()
                .filter(gate -> !gate.op.equals("XOR") && gate.out.charAt(0) == 'z')
                .filter(gate -> !gate.out.equals("z45"))
                .toList();

        var xy = List.of('x', 'y');
        var xnz = gates
                .stream()
                .filter(gate -> gate.op.equals("XOR"))
                .filter(gate -> !xy.contains(gate.left.charAt(0)))
                .filter(gate -> !xy.contains(gate.right.charAt(0)))
                .filter(gate -> gate.out.charAt(0) != 'z')
                .toList();

        for (var gate : xnz) {
            var b = nxz.stream().filter(it -> it.out.equals(foo(gate.out))).findFirst().orElseThrow();
            var temp = gate.out;
            gate.out = b.out;
            b.out = temp;
        }

        var x = convertNumber(initialData, s -> s.startsWith("x"));
        var y = convertNumber(initialData, s -> s.startsWith("y"));
        var z = convertNumber(solve(gates), s -> s.startsWith("z"));
        var falseCarry = (x + y) ^ Long.numberOfTrailingZeros(z);

        var remainingBadGates = gates
                .stream()
                ;
        var xsds = Stream
                .of(nxz.stream(), xnz.stream(), remainingBadGates)
                .flatMap(Function.identity())
                .map(g -> g.out)
                .sorted()
                .toList();
        return Joiner.on(",").join(xsds);
    }

    String foo(String s) {
        var options = gates.stream().filter(it -> it.left.equals(s) || it.right.equals(s));
        var a = options
                .filter(it -> it.out.startsWith("z"))
                .findFirst()
                .map(it -> {
                    var x = Integer.parseInt(it.out.substring(1)) - 1;
                    return "z" + String.format("%02d", x);
                });
        if (a.isPresent()) {
            return a.get();
        }

        return "";
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
