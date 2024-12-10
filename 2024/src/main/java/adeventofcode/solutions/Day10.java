package adeventofcode.solutions;

import adeventofcode.util.IntVec2;
import com.google.common.collect.Lists;
import io.vavr.control.Try;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class Day10 implements Solution {

    private final List<List<Integer>> topologyMap;

    Day10(String input) {
        try (var lines = Files.lines(Path.of(input))) {
            this.topologyMap = lines
                    .map(line -> Arrays.stream(line.split("")).map(Integer::parseInt).toList())
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public long one() {
        return startPoints().map(i -> nonRec(i).stream().distinct().count()).reduce(0L, Long::sum);
    }

    public long two() {
        return startPoints().map(i -> (long) nonRec(i).size()).reduce(0L, Long::sum);
    }

    private Stream<IntVec2> startPoints() {
        return IntVec2.indexStream(topologyMap).filter(i -> i.getValueFrom(topologyMap) == 0);
    }

    private List<IntVec2> nonRec(IntVec2 start) {
        var positions = List.of(start);
        for (var h = 1; h <= 9; h++) {
            final var height = h;
            positions = Lists
                    .cartesianProduct(positions, IntVec2.CARDINALS)
                    .stream()
                    .map(product -> product.getFirst().plus(product.getLast()))
                    .filter(position -> Try.of(() -> position.getValueFrom(topologyMap) == height).getOrElse(false))
                    .toList();
        }
        return positions;
    }
}
