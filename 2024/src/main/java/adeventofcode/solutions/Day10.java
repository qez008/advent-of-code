package adeventofcode.solutions;

import adeventofcode.util.IntVec2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class Day10 implements Solution {

    private static final int START_HEIGHT = 0;
    private static final int END_HEIGHT = 9;

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

    private Stream<IntVec2> startPoints() {
        return IntVec2.indexStream(topologyMap).filter(i -> i.getValueFrom(topologyMap) == START_HEIGHT);
    }

    private List<List<IntVec2>> pathsFrom(IntVec2 currentPosition, int currentHeight, List<IntVec2> path) {
        if (currentHeight == END_HEIGHT) {
            var newPath = new ArrayList<>(path);
            newPath.add(currentPosition);
            return List.of(newPath);
        }
        var options = IntVec2.CARDINALS.stream().map(currentPosition::plus).filter(i -> {
            try {
                return i.getValueFrom(topologyMap) == currentHeight + 1;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        });
        return options
                .map(i -> {
                    var newPath = new ArrayList<>(path);
                    newPath.add(i);
                    return pathsFrom(i, currentHeight + 1, newPath);
                })
                .reduce(List.of(), (a, b) -> Stream.concat(a.stream(), b.stream()).toList());
    }

    public long one() {
        return startPoints()
                .map(i -> pathsFrom(i, 0, List.of(i))
                        .stream()
                        .map(List::getLast)
                        .distinct()
                        .count())
                .reduce(0L, Long::sum);
    }

    public long two() {
        return startPoints().map(i -> (long) pathsFrom(i, 0, List.of(i)).size()).reduce(0L, Long::sum);
    }
}
