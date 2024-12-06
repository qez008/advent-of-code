package adeventofcode.solutions;

import adeventofcode.util.IntVec2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

class Day6 {

    private final List<ArrayList<Character>> grid;
    private final IntVec2 startPosition;

    Day6(String input) {
        try (var lines = Files.lines(Path.of(input))) {
            this.grid = lines.map(string -> {
                var list = string.chars().mapToObj((i) -> (char) i).toList();
                return new ArrayList<>(list);
            }).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.startPosition = IntVec2
                .indexStream(grid)
                .filter(i -> i.getValueFrom(grid) == '^')
                .findAny()
                .orElseThrow();
    }

    record Result(Set<PathEntry> path, boolean isLoop) {}

    record PathEntry(IntVec2 position, IntVec2 direction) {}

    Result pathFinder() {
        var position = startPosition;
        var direction = new IntVec2(0, -1);

        var path = new HashSet<PathEntry>();
        path.add(new PathEntry(startPosition, direction));

        try {
            while (true) {
                var nextPosition = position.plus(direction);
                if (nextPosition.getValueFrom(grid) == '#') {
                    direction = direction.rotateClockwise90();
                } else {
                    position = nextPosition;
                }
                var entry = new PathEntry(position, direction);
                if (path.contains(entry)) {
                    return new Result(path, true);
                }
                path.add(entry);
            }
        } catch (IndexOutOfBoundsException e) {
            return new Result(path, false);
        }
    }

    long one() {
        return pathFinder().path().stream().map(PathEntry::position).distinct().count();
    }

    long two() {
        var start = System.currentTimeMillis();
        Predicate<IntVec2> loopsPredicate = obstacleIndex -> {
            obstacleIndex.setValueIn(grid, '#');
            var result = pathFinder().isLoop();
            obstacleIndex.setValueIn(grid, '.');
            return result;
        };
        var visitedPositions = pathFinder().path().parallelStream().map(PathEntry::position).distinct();
        var ans = visitedPositions.filter(index -> index != startPosition).filter(loopsPredicate).count();
        System.out.println("et: " + (System.currentTimeMillis() - start));
        return ans;
    }
}
