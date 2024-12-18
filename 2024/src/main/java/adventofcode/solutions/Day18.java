package adventofcode.solutions;

import adventofcode.util.IntVector2;
import adventofcode.util.Rect;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
class Day18 implements Solution {

    private final IntVector2 exit;
    private final List<IntVector2> fallingBytes;

    public Integer part1() {
        return solve(fallingBytes.size() >= 1024 ? 1024 : 12).orElse(-1);
    }

    public String part2() {
        var range = new IntVector2(0, fallingBytes.size());

        while (range.x() != range.y() - 1) {
            var middle = range.x() + (range.y() - range.x()) / 2;
            range = solve(middle).isPresent() ? new IntVector2(middle, range.y()) : new IntVector2(range.x(), middle);
        }

        var result = fallingBytes.get(range.x());
        return result.x() + "," + result.y();
    }

    record State(IntVector2 pos, int steps) {

        int value(IntVector2 exit) {
            return steps + pos.manhattanDistance(exit);
        }
    }

    Optional<Integer> solve(int fallen) {
        var bounds = new Rect(IntVector2.ZERO, exit);
        var memorySpace = createSpace(exit, fallingBytes, fallen);

        var queue = new PriorityQueue<State>(Comparator.comparing(s -> s.value(exit)));
        queue.add(new State(IntVector2.ZERO, 0));

        var visited = new HashSet<IntVector2>();

        while (queue.poll() instanceof State(IntVector2 pos, int steps)) {
            if (pos.equals(exit)) {
                return Optional.of(steps);
            }
            for (var dir : IntVector2.CARDINAL_DIRECTIONS) {
                var nextPos = pos.plus(dir);
                if (visited.contains(nextPos)) {
                    continue;
                }
                if (!bounds.contains(nextPos) || pos.getValueFrom(memorySpace) == '#') {
                    continue;
                }
                queue.add(new State(nextPos, steps + 1));
            }
            visited.add(pos);
        }
        return Optional.empty();
    }

    List<ArrayList<Character>> createSpace(IntVector2 bounds, List<IntVector2> fallingBytes, int fallenBytes) {
        var memorySpace = new ArrayList<ArrayList<Character>>();
        for (var y = 0; y <= bounds.y(); y++) {
            var row = new ArrayList<Character>();
            for (var x = 0; x <= bounds.x(); x++) {
                row.add('.');
            }
            memorySpace.add(row);
        }
        for (var i = 0; i < fallenBytes; i++) {
            fallingBytes.get(i).setValueIn(memorySpace, '#');
        }
        return memorySpace;
    }
}
