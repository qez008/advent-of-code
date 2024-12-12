package adventofcode.solutions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

class Day5 {

    private final HashMap<Integer, List<Integer>> orderingRules;
    private final List<List<Integer>> updates;

    Day5(String input) {
        this.orderingRules = new HashMap<>();
        this.updates = new ArrayList<>();

        try {
            var lines = Files.readAllLines(Path.of(input));
            var splitIndex = IntStream.range(0, lines.size())
                    .filter(i -> lines.get(i).isBlank())
                    .findAny()
                    .orElseThrow();


            for (var line : lines.subList(0, splitIndex)) {
                var pages = Arrays.stream(line.split("\\|")).map(Integer::parseInt).toList();

                if (!orderingRules.containsKey(pages.getFirst())) {
                    orderingRules.put(pages.get(0), new ArrayList<>());
                }
                orderingRules.get(pages.get(0)).add(pages.get(1));
            }

            for (var line : lines.subList(splitIndex + 1, lines.size())) {
                var update = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
                updates.add(update);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    long one() {
        return updates
                .stream()
                .filter(update -> inOrder(update, orderingRules))
                .map(update -> (long) update.get(update.size() / 2))
                .reduce(0L, Long::sum);
    }

    private boolean inOrder(List<Integer> update, Map<Integer, List<Integer>> rules) {
        var seen = new HashSet<Integer>();
        for (var page : update) {
            for (var mustUpdateAfter : rules.getOrDefault(page, List.of())) {
                if (seen.contains(mustUpdateAfter)) {
                    return false;
                }
            }
            seen.add(page);
        }
        return true;
    }

    long two() {
        return updates
                .stream()
                .filter(update -> !inOrder(update, orderingRules))
//                .map(brokenUpdate -> fixUpdate(brokenUpdate, orderingRules))
                .map(updates -> updates.stream().sorted((pageA, pageB) -> {
                    if (orderingRules.containsKey(pageA)) {
                        return orderingRules.get(pageA).contains(pageB) ? -1 : 1;
                    }
                    return 0;
                }).toList())
                .map(update -> (long) update.get(update.size() / 2))
                .reduce(0L, Long::sum);
    }

    private List<Integer> fixUpdate(List<Integer> brokenUpdate, HashMap<Integer, List<Integer>> orderingRules) {
        var fixedUpdate = new ArrayList<>(brokenUpdate);

        while (!inOrder(fixedUpdate, orderingRules)) {
            var seen = new HashSet<Integer>();

            for (var index = 0; index < fixedUpdate.size(); index++) {
                var page = fixedUpdate.get(index);
                var tempIndex = index;
                for (var mustUpdateAfter : orderingRules.getOrDefault(page, List.of())) {
                    if (seen.contains(mustUpdateAfter)) {
                        fixedUpdate.remove(tempIndex);
                        var newIndex = fixedUpdate.indexOf(mustUpdateAfter);
                        fixedUpdate.add(newIndex, page);
                        tempIndex = newIndex;
                    }
                }
                seen.add(page);
            }
        }
        return fixedUpdate;
    }
}
