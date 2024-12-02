package adeventofcode;

import com.google.common.collect.Streams;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
class Day1 {

    private final List<Integer> listA;
    private final List<Integer> listB;

    Day1(String location) {
        this.listA = new ArrayList<>();
        this.listB = new ArrayList<>();

        var path = Paths.get(location);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            while (reader.readLine() instanceof String line) {
                var parts = line.split(" {3}");
                listA.add(Integer.parseInt(parts[0]));
                listB.add(Integer.parseInt(parts[1]));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    int one() {
        var sortedA = listA.stream().sorted();
        var sortedB = listB.stream().sorted();
        return Streams
                .zip(sortedA, sortedB, (a, b) -> Math.abs(a - b))
                .reduce(0, Integer::sum);
    }

    long two() {
        var counts = listB.stream().collect(Collectors.toMap(
                Function.identity(), b -> 1, (v, b) -> v + 1));
        return listA
                .stream()
                .map(a -> counts.get(a) instanceof Integer count ? a * count : 0)
                .reduce(0, Integer::sum);
    }
}