package adventofcode.solutions;

import adventofcode.util.IntVector2;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public class Day12 implements Solution {

    private final List<List<Character>> garden;

    public long part1() {
        return mapPlot((plot, edges) -> {
            var area = plot.size();
            var perimeter = edges.size();
            return (long) area * (long) perimeter;
        });
    }

    public long part2() {
        return mapPlot((plot, edges) -> {
            var area = plot.size();
            var sides = countSides(edges);
            return (long) sides * (long) area;
        });
    }

    record PlotEdge(IntVector2 position, IntVector2 direction) {}


    long mapPlot(BiFunction<HashSet<IntVector2>, HashSet<PlotEdge>, Long> solver) {
        var visited = new HashSet<IntVector2>();
        var total = 0L;

        for (var index : IntVector2.indexStream(garden).toList()) {
            if (visited.contains(index)) {
                continue;
            }
            var plot = new HashSet<IntVector2>();
            var edges = new HashSet<PlotEdge>();
            floodFill(index, plot, edges);

            total += solver.apply(plot, edges);

            visited.addAll(plot);
        }
        return total;
    }

    private void floodFill(IntVector2 position,
                           HashSet<IntVector2> plot,
                           HashSet<PlotEdge> edges) {

        if (plot.contains(position)) {
            return;
        }
        plot.add(position);

        for (var direction : IntVector2.CARDINAL_DIRECTIONS) {
            var neighbour = position.plus(direction);
            try {
                if (neighbour.getValueFrom(garden) == position.getValueFrom(garden)) {
                    floodFill(neighbour, plot, edges);
                } else {
                    edges.add(new PlotEdge(position, direction));
                }
            } catch (IndexOutOfBoundsException e) {
                edges.add(new PlotEdge(position, direction));
            }
        }
    }

    int countSides(HashSet<PlotEdge> edges) {
        var sides = 0;
        var visited = new HashSet<PlotEdge>();

        for (var edge : edges) {
            if (visited.contains(edge)) {
                continue;
            }
            sides++;

            for (var perpendicularDirection : edge.direction().perpendicular()) {
                var currentPosition = edge.position();
                while (true) {
                    currentPosition = currentPosition.plus(perpendicularDirection);
                    var currentEdge = new PlotEdge(currentPosition, edge.direction());
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
