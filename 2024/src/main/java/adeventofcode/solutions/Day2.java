package adeventofcode.solutions;

import adeventofcode.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

class Day2 {

    private final List<List<Integer>> reports;

    Day2(String input) {
        this.reports = new ArrayList<>();
        Util.fileForEach(input, line -> {
            var report = Arrays.stream(line.split(" ")).map(Integer::parseInt).toList();
            reports.add(report);
        });
    }

    long one() {
        return reports.stream().map(this::createDiffs).filter(this::isSafe).count();
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
                        .map(this::createDiffs))
                .filter(alts -> alts.anyMatch(this::isSafe))
                .count();
    }

    private List<Integer> createDiffs(List<Integer> report) {
        var diffs = new ArrayList<Integer>();
        for (var j = 0; j < report.size() - 1; j++) {
            diffs.add(report.get(j + 1) - report.get(j));
        }
        return diffs;
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

    private static Integer sign(int x) {
        return x < 0 ? -1 : 1;
    }
}
