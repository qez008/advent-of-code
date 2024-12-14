package adventofcode.solutions;

import adventofcode.util.IntVector2;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class Day12 implements Solution {

    private final List<List<Character>> garden;

    public long part1() {
        return mapRegion((region, edges) -> {
            var area = region.size();
            var perimeter = edges.size();
            return (long) area * (long) perimeter;
        });
    }

    public long part2() {
        return mapRegion((region, edges) -> {
            var area = region.size();
            var sides = countSides(edges);
            return (long) sides * (long) area;
        });
    }

    record ReqionEdge(IntVector2 position, IntVector2 direction) {}


    long mapRegion(BiFunction<HashSet<IntVector2>, HashSet<ReqionEdge>, Long> solver) {
        var visited = new HashSet<IntVector2>();
        var total = 0L;

        for (var index : IntVector2.indexStream(garden).toList()) {
            if (visited.contains(index)) {
                continue;
            }
            var region = new HashSet<IntVector2>();
            var edges = new HashSet<ReqionEdge>();
            floodFillNonRec(index, region, edges);

            total += solver.apply(region, edges);

            visited.addAll(region);
        }
        return total;
    }

    private void floodFill(IntVector2 plot,
                           HashSet<IntVector2> region,
                           HashSet<ReqionEdge> edges) {

        if (region.contains(plot)) {
            return;
        }
        region.add(plot);

        for (var direction : IntVector2.CARDINAL_DIRECTIONS) {
            var neighbour = plot.plus(direction);
            try {
                if (neighbour.getValueFrom(garden) == plot.getValueFrom(garden)) {
                    floodFill(neighbour, region, edges);
                } else {
                    edges.add(new ReqionEdge(plot, direction));
                }
            } catch (IndexOutOfBoundsException e) {
                edges.add(new ReqionEdge(plot, direction));
            }
        }
    }

    private void floodFillNonRec(IntVector2 startPosition,
                                 HashSet<IntVector2> region,
                                 HashSet<ReqionEdge> edges) {

        var queue = new LinkedList<IntVector2>();
        queue.add(startPosition);

        while (!queue.isEmpty()) {
            var plot = queue.pop();
            if (region.contains(plot)) {
                continue;
            }
            region.add(plot);
            for (var direction : IntVector2.CARDINAL_DIRECTIONS) {
                var neighbourPlot = plot.plus(direction);
                try {
                    if (neighbourPlot.getValueFrom(garden) == plot.getValueFrom(garden)) {
                        queue.add(neighbourPlot);
                    } else {
                        edges.add(new ReqionEdge(plot, direction));
                    }
                } catch (IndexOutOfBoundsException e) {
                    edges.add(new ReqionEdge(plot, direction));
                }
            }
        }
    }


    int countSides(HashSet<ReqionEdge> edges) {
        var sides = 0;
        var visited = new HashSet<ReqionEdge>();

        for (var edge : edges) {
            if (visited.contains(edge)) {
                continue;
            }
            sides++;

            for (var perpendicularDirection : edge.direction().perpendicular()) {
                var currentPosition = edge.position();
                while (true) {
                    currentPosition = currentPosition.plus(perpendicularDirection);
                    var currentEdge = new ReqionEdge(currentPosition, edge.direction());
                    if (!edges.contains(currentEdge)) {
                        break;
                    }
                    visited.add(currentEdge);
                }
            }
        }
        return sides;
    }
}
