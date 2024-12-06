package adeventofcode.solutions;

import adeventofcode.util.IntVec2;
import lombok.With;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

class Day4 {

    private static final List<Vec2> cardinals = List.of(
            new Vec2(1, 0), new Vec2(0, 1), new Vec2(-1, 0), new Vec2(0, -1));
    private static final List<Vec2> diagonals = List.of(
            new Vec2(1, 1), new Vec2(-1, 1), new Vec2(-1, -1), new Vec2(1, -1));

    private final List<List<Character>> grid;
    private final List<IntVec2> indices;

    Day4(String path) throws IOException {
        this.grid = Files
                .readAllLines(Path.of(path))
                .stream()
                .map(string -> string.chars().mapToObj(x -> (char) x).toList())
                .toList();
        this.indices = IntVec2.indexStream(grid).toList();
    }

    private record Vec2(@With int x, @With int y) {
        Vec2 plus(Vec2 other) {
            return new Vec2(x + other.x, y + other.y);
        }
        Vec2 reverse() {
            return new Vec2(-x, -y);
        }
        <T> T getValue(List<List<T>> grid) {
            return grid.get(y()).get(x());
        }
    }

    long one() {
        return indices
                .stream()
                .map(i -> Stream
                        .concat(cardinals.stream(), diagonals.stream())
                        .filter(dir -> isStartOfXmas(new Vec2(i.x(), i.y()), dir))
                        .count())
                .reduce(0L, Long::sum);
    }

    long two() {
        return indices
                .stream()
                .map(index -> new Vec2(index.x(), index.y()))
                .filter(position -> position.getValue(grid).equals('A'))
                .map(position -> isMasCross(position) ? 1 : 0)
                .reduce(0, Integer::sum);
    }


    private boolean isStartOfXmas(Vec2 position, Vec2 direction) {
        for (var c : List.of('X', 'M', 'A', 'S')) {
            try {
                if (position.getValue(grid).equals(c)) {
                    position = position.plus(direction);
                } else {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }

    private boolean isMasCross(Vec2 position) {
        Function<Vec2, Boolean> diagonalIsMas = direction -> {
            try {
                return position.plus(direction.reverse()).getValue(grid).equals('M')
                       && position.plus(direction).getValue(grid).equals('S');
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        };
        return 2L == diagonals.stream().filter(diagonalIsMas::apply).count();
    }
}
