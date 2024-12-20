package adventofcode.solutions;

import adventofcode.util.IntVector2;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
class Day20 implements Solution {

    private final List<String> raceTrack;

    public Integer part1() {
        var path = findPath();
        var heatmap = createHeatmap(path);

        return (int) path.stream().flatMap(x -> findCheats(x, heatmap)).filter(i -> i >= 100).count();
    }

    public Integer part2() {
        var path = findPath();
        var heatmap = createHeatmap(path);

        var cheats = path.stream().flatMap(x -> findCheatsV2(x, heatmap).stream()).filter(x -> x >= 50).toList();
        return (int) cheats.stream().filter(i -> i >= 100).count();
    }

    List<IntVector2> findPath() {
        var start = IntVector2
                .indexStream(raceTrack.size(), raceTrack.getFirst().length())
                .filter(x -> x.charFrom(raceTrack) == 'S')
                .findAny()
                .orElseThrow();
        var path = new ArrayList<IntVector2>();
        path.add(start);

        while (true) {
            var pos = path.getLast();
            for (var dir : IntVector2.CARDINAL_DIRECTIONS) {
                var next = pos.plus(dir);
                var cell = next.charFrom(raceTrack);
                if (cell == 'E') {
                    path.add(next);
                    return path;
                }
                if (cell == '#') {
                    continue;
                }
                if (!path.contains(next)) {
                    path.add(next);
                    break;
                }
            }
        }
    }

    private HashMap<IntVector2, Integer> createHeatmap(List<IntVector2> path) {
        var heatmap = new HashMap<IntVector2, Integer>();
        for (var i = 0; i < path.size(); i++) {
            heatmap.put(path.get(i), path.size() - 1 - i);
        }
        return heatmap;
    }

    private Stream<Integer> findCheats(IntVector2 pos, HashMap<IntVector2, Integer> hm) {
        return IntVector2.CARDINAL_DIRECTIONS
                .stream()
                .filter(dir -> pos.plus(dir).charFrom(raceTrack) == '#')
                .filter(dir -> hm.containsKey(pos.plus(dir.times(2))))
                .map(dir -> hm.get(pos) - hm.get(pos.plus(dir.times(2))) - 2)
                .filter(cheat -> cheat >= 0);
    }

    private List<Integer> findCheatsV2(IntVector2 start, HashMap<IntVector2, Integer> heatmap) {
        var startPositions = IntVector2.CARDINAL_DIRECTIONS
                .stream()
                .map(start::plus)
                .toList();

        var queue = new LinkedList<>(startPositions);
        var visited = new HashSet<IntVector2>();
        var result = new ArrayList<Integer>();

        for (var i = 1; i <= 20; i++) {
            if (queue.isEmpty()) {
                break;
            }
            var nextQueue = new LinkedList<IntVector2>();
            for (var pos : queue) {
                if (visited.contains(pos)) {
                    continue;
                }
                if (heatmap.get(pos) instanceof Integer value) {
                    var saved = heatmap.get(start) - value - i;
                    if (saved > 0) {
                        result.add(saved);
                    }
                }
                for (var dir : IntVector2.CARDINAL_DIRECTIONS) {
                    var next = pos.plus(dir);
                    if (visited.contains(next)) {
                        continue;
                    }
                    nextQueue.add(next);
                }
                visited.add(pos);
            }
            queue = nextQueue;
        }
        return result;
    }
}
