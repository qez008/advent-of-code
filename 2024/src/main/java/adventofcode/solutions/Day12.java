package adventofcode.solutions;

import adventofcode.util.IntVector2;
import io.vavr.Function3;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class Day12 implements Solution {

    private final List<List<Character>> garden;

    private boolean isVisited(IntVector2 index, Boolean[][] visitedMap) {
        return Objects.requireNonNullElse(visitedMap[index.y()][index.x()], false);
    }

    private void markVisited(IntVector2 index, Boolean[][] visitedMap) {
        visitedMap[index.y()][index.x()] = true;
    }

    public long part1() {
        return mapPlot((index, crop, plot) -> {
            var area = plot.size();
            var perimeter = calculatePerimeter(plot);
            return (long) area * (long) perimeter;
        });
    }

    private long mapPlot(Function3<IntVector2, Character, HashSet<IntVector2>, Long> solver) {
        var visited = new Boolean[garden.size()][garden.getFirst().size()];
        var total = 0L;

        for (var index : IntVector2.indexStream(garden).toList()) {
            if (!isVisited(index, visited)) {
                var crop = index.getValueFrom(garden);
                var plot = new HashSet<IntVector2>();
                floodFill(index, crop, visited, plot);

                total += solver.apply(index, crop, plot);
            }
        }
        return total;
    }

    private void floodFill(IntVector2 index, char crop, Boolean[][] visitedMap, HashSet<IntVector2> plot) {
        if (isVisited(index, visitedMap) || index.getValueFrom(garden) != crop) {
            return;
        }
        markVisited(index, visitedMap);
        plot.add(index);
        for (var direction : IntVector2.CARDINAL_DIRECTIONS) {
            var neightbour = index.plus(direction);
            try {
                floodFill(neightbour, crop, visitedMap, plot);
            } catch (IndexOutOfBoundsException e) {
                // do nothing
            }
        }
    }

    private int calculatePerimeter(Set<IntVector2> plot) {
        int perimeter = 0;
        for (IntVector2 p : plot) {
            for (var dir : IntVector2.CARDINAL_DIRECTIONS) {
                IntVector2 neighbor = p.plus(dir);
                if (!plot.contains(neighbor)) {
                    perimeter++;
                }
            }
        }
        return perimeter;
    }

    public long part2() {
        return mapPlot((index, crop, plot) -> {
            var area = plot.size();
            var sides = cornerCount(new ArrayList<>(plot), crop);
            return (long) sides * (long) area;
        });
    }

    record PlotEdge(IntVector2 position, IntVector2 direction) {}

    int cornerCount(List<IntVector2> plot, char crop) {
        var edges = new HashSet<PlotEdge>();

        for (var point : plot) {
            for (var dir : IntVector2.CARDINAL_DIRECTIONS) {
                var neighbor = point.plus(dir);
                var isEdge = Try.of(() -> neighbor.getValueFrom(garden) != crop).getOrElse(true);
                if (isEdge) {
                    edges.add(new PlotEdge(point, dir));
                }
            }
        }

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
