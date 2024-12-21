package adventofcode.solutions;

import adventofcode.util.IntVector2;
import com.google.common.base.Joiner;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class Day21 implements Solution {

    static final List<String> NUM_PAD = List.of(
            "789",
            "456",
            "123",
            " 0A"
    );

    static final List<String> DIR_PAD = List.of(
            " ^A",
            "<v>"
    );

    static final char NO_BUTTON = ' ';

    static final IntVector2 NUM_START = new IntVector2(2, 3);
    static final IntVector2 DIR_START = new IntVector2(2, 0);

    private final List<String> codes;

    public Long part1() {
        return solve(2);
    }

    public Long part2() {
        return solve(25);
    }

    Long solve(int layers) {
        var ans = 0L;
        for (var code : codes) {
            var minA = findPaths(NUM_PAD, code, NUM_START, new ArrayList<>())
                    .stream()
                    .map(p -> findMinLen(layers, pathToString(p), new HashMap<>()))
                    .min(Comparator.naturalOrder())
                    .orElseThrow();
            var numericPart = Long.parseLong(code.substring(0, code.length() - 1));
            ans += minA * numericPart;
        }
        return ans;
    }

    List<? extends List<Character>> findPaths(
            List<String> pad, String seq, IntVector2 from, ArrayList<Character> path) {

        if (seq.isEmpty()) {
            return List.of(new ArrayList<>(path));
        }

        var to = IntVector2
                .indexStream(pad.size(), pad.getFirst().length())
                .filter(v -> v.charFrom(pad) == seq.charAt(0))
                .findAny()
                .orElseThrow(() -> new RuntimeException("failed to find target"));

        if (from.equals(to)) {
            path.add('A');
            var res = findPaths(pad, seq.substring(1), from, path);
            path.removeLast();
            return res;
        } else {
            var res = new ArrayList<List<Character>>();
            for (var dir : IntVector2.CARDINAL_DIRECTIONS) {
                var nextPos = from.plus(dir);
                var cell = Try.of(() -> nextPos.charFrom(pad)).getOrElse(NO_BUTTON);
                if (cell != NO_BUTTON && nextPos.manhattanDistance(to) <= from.manhattanDistance(to)) {
                    path.add(dir.toChar());
                    res.addAll(findPaths(pad, seq, nextPos, path));
                    path.removeLast();
                }
            }
            return res;
        }
    }

    record MemKey(int level, String code) {}

    long findMinLen(int level, String sequence, HashMap<MemKey, Long> memo) {
        if (level == 0) {
            return sequence.length();
        }
        if (memo.get(new MemKey(level, sequence)) instanceof Long value) {
            return value;
        }
        var totalLength = 0L;
        var start = DIR_START;
        for (var key : sequence.toCharArray()) {
            var minLength = Long.MAX_VALUE;
            for (var path : findPaths(DIR_PAD, "" + key, start, new ArrayList<>())) {
                var count = findMinLen(level - 1, pathToString(path), memo);
                if (count < minLength) {
                    minLength = count;
                }
            }
            start = IntVector2
                    .indexStream(DIR_PAD.size(), DIR_PAD.getFirst().length())
                    .filter(x -> x.charFrom(DIR_PAD) == key)
                    .findAny()
                    .orElseThrow();
            totalLength += minLength;
        }
        memo.put(new MemKey(level, sequence), totalLength);
        return totalLength;
    }

    String pathToString(List<Character> path) {
        return Joiner.on("").join(path);
    }
}
