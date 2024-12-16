package adventofcode.util;

import io.vavr.Tuple2;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AocUtil {

    private AocUtil() {}

    public static void fileForEach(String input, Consumer<String> task) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(input))) {
            while (reader.readLine() instanceof String line) {
                task.accept(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <A, B> Stream<Tuple2<A, B>> cartesianProduct(Collection<A> listA, Collection<B> listB) {
        return listA.stream().flatMap(a -> listB.stream().map(b -> new Tuple2<>(a, b)));
    }

}
