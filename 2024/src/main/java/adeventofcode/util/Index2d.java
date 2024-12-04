package adeventofcode.util;

import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Index2d(int x, int y) {

    public static Stream<Index2d> createStream(int height, int width) {
        return IntStream
                .range(0, height)
                .mapToObj(y -> IntStream.range(0, width).mapToObj(x -> new Index2d(x, y)))
                .flatMap(Function.identity());
    }
}
