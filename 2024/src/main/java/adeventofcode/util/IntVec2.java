package adeventofcode.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record IntVec2(int x, int y) {

    public IntVec2 rotateClockwise90() {
        return new IntVec2(-y, x);
    }

    public IntVec2 plus(IntVec2 other) {
        return new IntVec2(x + other.x, y + other.y);
    }

    public <T> T getValueFrom(List<? extends List<T>> grid) {
        return grid.get(y).get(x);
    }

    public <T> void setValueIn(List<ArrayList<T>> grid, T c) {
        grid.get(y).set(x, c);
    }

    public static Stream<IntVec2> indexStream(int height, int width) {
        return IntStream
                .range(0, height)
                .mapToObj(y -> IntStream.range(0, width).mapToObj(x -> new IntVec2(x, y)))
                .flatMap(Function.identity());
    }

    public static Stream<IntVec2> indexStream(List<? extends List<?>> grid) {
        return IntStream
                .range(0, grid.size())
                .mapToObj(y -> IntStream.range(0, grid.get(y).size()).mapToObj(x -> new IntVec2(x, y)))
                .flatMap(Function.identity());
    }
}
