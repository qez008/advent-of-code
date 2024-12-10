package adeventofcode.solutions;

import adeventofcode.util.AocUtil;
import com.google.common.collect.Streams;

import java.util.ArrayList;
import java.util.Arrays;
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

        AocUtil.fileForEach(location, line -> {
            var parts = Arrays.stream(line.split(" {3}")).map(Integer::parseInt).toList();
            listA.add(parts.get(0));
            listB.add(parts.get(1));
        });
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
