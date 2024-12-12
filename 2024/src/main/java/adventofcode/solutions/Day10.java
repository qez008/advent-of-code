package adventofcode.solutions;

import adventofcode.util.AocUtil;
import adventofcode.util.IntVector2;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class Day10 implements Solution {

    private final List<List<Integer>> topologyMap;

    public long part1() {
        return startPoints().map(i -> nonRec(i).stream().distinct().count()).reduce(0L, Long::sum);
    }

    public long part2() {
        return startPoints().map(i -> (long) nonRec(i).size()).reduce(0L, Long::sum);
    }

    private Stream<IntVector2> startPoints() {
        return IntVector2.indexStream(topologyMap).filter(i -> i.getValueFrom(topologyMap) == 0);
    }

    private List<IntVector2> nonRec(IntVector2 start) {
        var positions = List.of(start);
        for (var h = 1; h <= 9; h++) {
            final var height = h;
            positions = AocUtil
                    .cartesianProduct(positions, IntVector2.CARDINALS)
                    .map(product -> product.apply(IntVector2::plus))
                    // This may throw an index out-of-bounds exceptions in which case the
                    // position does not have the height we are looking for.
                    .filter(position -> Try
                            .of(() -> position.getValueFrom(topologyMap) == height)
                            .getOrElse(false))
                    .toList();
        }
        return positions;
    }
}
