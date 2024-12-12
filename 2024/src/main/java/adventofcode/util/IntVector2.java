package adventofcode.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record IntVector2(int x, int y) {

    public static final IntVector2 UP = new IntVector2(0, -1);
    public static final IntVector2 DOWN = new IntVector2(0, 1);
    public static final IntVector2 LEFT = new IntVector2(-1, 0);
    public static final IntVector2 RIGHT = new IntVector2(1, 0);

    public static final List<IntVector2> CARDINAL_DIRECTIONS = List.of(UP, DOWN, LEFT, RIGHT);

    public IntVector2 plus(IntVector2 other) {
        return new IntVector2(x + other.x, y + other.y);
    }

    public IntVector2 minus(IntVector2 b) {
        return new IntVector2(x - b.x, y - b.y);
    }

    public IntVector2 times(int amplitude) {
        return new IntVector2(x * amplitude, y * amplitude);
    }

    public IntVector2 rotateClockwise90() {
        return new IntVector2(-y, x);
    }

    public List<IntVector2> perpendicular() {
        return this == UP || this == DOWN
                ? List.of(LEFT, RIGHT)
                : List.of(UP, DOWN);
    }

    public <T> T getValueFrom(List<? extends List<T>> grid) {
        return grid.get(y).get(x);
    }

    public <T> void setValueIn(List<ArrayList<T>> grid, T c) {
        grid.get(y).set(x, c);
    }

    public static Stream<IntVector2> indexStream(int height, int width) {
        return IntStream
                .range(0, height)
                .mapToObj(y -> IntStream.range(0, width).mapToObj(x -> new IntVector2(x, y)))
                .flatMap(Function.identity());
    }

    public static Stream<IntVector2> indexStream(List<? extends List<?>> grid) {
        return IntStream
                .range(0, grid.size())
                .mapToObj(y -> IntStream.range(0, grid.get(y).size()).mapToObj(x -> new IntVector2(x, y)))
                .flatMap(Function.identity());
    }
}
