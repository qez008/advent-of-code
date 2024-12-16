package adventofcode.solutions;

import adventofcode.util.IntVector2;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
class Day16 implements Solution {

    private final List<List<Character>> grid;
    private final IntVector2 start;
    private final IntVector2 end;

    record State(IntVector2 pos, IntVector2 dir, long cost, List<IntVector2> path) {}

    record PosAndDir(IntVector2 pos, IntVector2 dir) {}

    record Result(long cost, long seats) {}

    private Result cheapestPathV2() {
        var queue = new PriorityQueue<>(Comparator.comparingLong(State::cost));
        queue.add(new State(start, IntVector2.RIGHT, 0L, List.of(start)));
        var visited = new HashMap<PosAndDir, Long>();

        Long best = null;
        var seats = new HashSet<IntVector2>();

        while (queue.poll() instanceof State(IntVector2 pos, IntVector2 dir, long cost, List<IntVector2> path)) {
            if (pos.equals(end)) {
                if (best == null || cost < best) {
                    best = cost;
                    seats = new HashSet<>(path);
                } else if (cost == best) {
                    seats.addAll(path);
                } else {
                    break;
                }
            }
            visited.put(new PosAndDir(pos, dir), cost);

            for (var nextDir : List.of(dir, dir.rotateClockwise90(), dir.rotateCounterClockwise90())) {
                var nextPos = pos.plus(nextDir);
                if (nextPos.getValueFrom(grid) == '#') {
                    continue;
                }
                long nextCost = cost + (nextDir.equals(dir) ? 1 : 1001);
                var nextPath = new ArrayList<>(path);
                nextPath.add(nextPos);
                var state = new State(nextPos, nextDir, nextCost, nextPath);

                Long previous = visited.get(new PosAndDir(nextPos, nextDir));
                if (previous == null || previous > nextCost) {
                    queue.add(state);
                }
            }
        }
        return new Result(best, seats.size());
    }

    public long part1() {
        return cheapestPathV2().cost;
    }

    public long part2() {
        return cheapestPathV2().seats;
    }
}
