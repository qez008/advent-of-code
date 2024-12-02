package adeventofcode;

import org.checkerframework.common.value.qual.IntRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class Day2 {

    private static Integer sign(int x) {
        return x < 0 ? -1 : 1;
    }

    private final List<List<Integer>> reports;

    Day2(String input) {
        this.reports = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(input))) {
            while (reader.readLine() instanceof String line) {
                reports.add(Arrays.stream(line.split(" ")).map(Integer::parseInt).toList());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    long one() {
        return reports
                .stream()
                .map(report -> {
                    var diffs = new ArrayList<Integer>();
                    for (var i = 0; i < report.size() - 1; i++) {
                        diffs.add(report.get(i) - report.get(i + 1));
                    }
                    return diffs;
                })
                .filter(this::isSafe)
                .count();
    }

    long two() {
        return reports
                .stream()
                .map(report -> IntStream
                        .rangeClosed(0, report.size())
                        .mapToObj(i -> {
                            if (i == report.size()) {
                                return report;
                            }
                            var modifiedReport = new ArrayList<>(report);
                            modifiedReport.remove(i);
                            return modifiedReport;
                        })
                        .map(modifiedReport -> {
                            var diffs = new ArrayList<Integer>();
                            for (var j = 0; j < modifiedReport.size() - 1; j++) {
                                diffs.add(modifiedReport.get(j + 1) - modifiedReport.get(j));
                            }
                            return diffs;
                        }))
                .filter(alts -> alts.anyMatch(this::isSafe))
                .count();
    }

    private boolean isSafe(List<Integer> diffs) {
        var direction = sign(diffs.getFirst());
        for (var diff : diffs) {
            if (!sign(diff).equals(direction) || Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false;
            }
        }
        return true;
    }
}
