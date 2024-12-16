package adventofcode.solutions;

import adventofcode.util.IntVector2;
import io.vavr.Tuple2;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
class Day16 implements Solution {

    private final List<List<Character>> grid;
    private final IntVector2 start;
    private final IntVector2 end;

    record State(IntVector2 pos, IntVector2 dir, long cost, List<IntVector2> path) {}

    record Result(long cost, long seats) {}

    private Result cheapestPathV2() {
        var queue = new PriorityQueue<>(Comparator.comparingLong(State::cost));
        queue.add(new State(start, IntVector2.RIGHT, 0L, List.of(start)));
        var visited = new HashMap<Tuple2<IntVector2, IntVector2>, Long>();

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
            visited.put(new Tuple2<>(pos, dir), cost);

            var next = Stream
                    .of(dir, dir.rotateClockwise90(), dir.rotateCounterClockwise90())
                    .map(d -> new Tuple2<>(pos.plus(d), d))
                    .filter(t -> t.apply((p, d) -> p.getValueFrom(grid) != '#'))
                    .toList();

            for (var n : next) {
                long c = cost + (n._2.equals(dir) ? 1 : 1001);
                var p = new ArrayList<>(path);
                p.add(n._1);
                var state = new State(n._1, n._2, c, p);
                Long previous = visited.get(n);
                if (previous == null || previous > c) {
                    queue.add(state);
                }
            }
        }
        return new Result(best, seats.stream().distinct().count());
    }

    public long part1() {
        return cheapestPathV2().cost;
    }

    public long part2() {
        return cheapestPathV2().seats;
    }
}
