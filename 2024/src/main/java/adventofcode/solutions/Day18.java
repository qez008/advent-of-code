package adventofcode.solutions;

import adventofcode.util.IntVector2;
import adventofcode.util.Rect;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
class Day18 implements Solution {

    private final IntVector2 exit;
    private final List<IntVector2> fallingBytes;

    record State(IntVector2 pos, List<IntVector2> path) {

        int value(IntVector2 exit) {
            return path.size() + pos.manhattanDistance(exit);
        }
    }

    public long part1() {
        return solve(1024).orElse(0L);
    }

    public long part2() {
        var range = new IntVector2(0, fallingBytes.size());

        while (range.x() != range.y() - 1) {
            var middle = range.x() + (range.y() - range.x()) / 2;
            range = solve(middle).isPresent()
                    ? new IntVector2(middle, range.y())
                    : new IntVector2(range.x(), middle);
        }

        System.out.println(fallingBytes.get(range.x()));
        return range.x();
    }

    private Optional<Long> solve(int fallen) {
        var bounds = new Rect(IntVector2.ZERO, exit);
        var memorySpace = createSpace(exit, fallingBytes, fallen);

        var queue = new PriorityQueue<State>(Comparator.comparing(s -> s.value(exit)));
        queue.add(new State(IntVector2.ZERO, List.of(IntVector2.ZERO)));

        var visited = new HashSet<IntVector2>();

        while (queue.poll() instanceof State(IntVector2 pos, List<IntVector2> path)) {
            if (pos.equals(exit)) {
//                for (var point : path) {
//                    point.setValueIn(memorySpace, 'O');
//                }
//                exit.setValueIn(memorySpace, 'O');
//                printMemorySpace(memorySpace);

                return Optional.of(path.size() - 1L);
            }

            for (var dir : IntVector2.CARDINAL_DIRECTIONS) {
                var nextPos = pos.plus(dir);
                if (visited.contains(nextPos)) {
                    continue;
                }
                var nextPath = Stream.concat(path.stream(), Stream.of(pos)).toList();

                if (!bounds.contains(nextPos) || pos.getValueFrom(memorySpace) == '#') {
                    continue;
                }
                queue.add(new State(nextPos, nextPath));
            }
            visited.add(pos);
        }

        return Optional.empty();
    }

    private List<ArrayList<Character>> createSpace(IntVector2 bounds,
                                                   List<IntVector2> fallingBytes,
                                                   int fallenBytes) {

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

    private <T> void printMemorySpace(List<? extends List<T>> memorySpace) {
        for (var row : memorySpace) {
            System.out.println(Joiner.on(" ").join(row));
        }
    }
}
