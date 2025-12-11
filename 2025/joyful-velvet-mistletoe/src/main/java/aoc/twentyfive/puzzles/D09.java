package aoc.twentyfive.puzzles;

import aoc.twentyfive.common.InputUtil;
import aoc.twentyfive.common.Vec2;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class D09 extends PuzzleSolver<List<Vec2<Long>>> {

    @Override public List<Vec2<Long>> parseInput() {
        return InputUtil
                .getInput(9)
                .stream()
                .map(line -> {
                    var splitLine = line.split(",");
                    return new Vec2<>(
                            Long.parseLong(splitLine[0]),
                            Long.parseLong(splitLine[1])
                    );
                })
                .toList();
    }

    @Override public long part1(List<Vec2<Long>> redTiles) {
        return allRectangles(redTiles).getFirst().area();
    }

    // too low: 1568156
    @Override public long part2(List<Vec2<Long>> redTiles) {
        var allX = redTiles.stream().map(Vec2::x).collect(Collectors.toSet());
        var allY = redTiles.stream().map(Vec2::y).collect(Collectors.toSet());

        var uniqueX = allX.stream().sorted().toList();
        var uniqueY = allY.stream().sorted().toList();

        var xMap = new HashMap<Long, Long>();
        var yMap = new HashMap<Long, Long>();

        for (var i = 0; i < uniqueX.size(); i++) {
            xMap.put(uniqueX.get(i), (long) i);
        }
        for (var i = 0; i < uniqueY.size(); i++) {
            yMap.put(uniqueY.get(i), (long) i);
        }

        var grid = new char[uniqueY.size()][uniqueX.size()];
        for (var i = 0; i < grid.length; i++) {
            for (var j = 0; j < grid[0].length; j++) {
                grid[i][j] = '.';
            }
        }

        var zPoints = redTiles
                .stream()
                .map(p -> {
                    var y = yMap.get(p.y());
                    var x = xMap.get(p.x());
                    grid[y.intValue()][x.intValue()] = '#';
                    return new Vec2<>(x, y);
                })
                .toList();

        var a = zPoints.getLast();
        for (var b : zPoints) {
            if (a.x().equals(b.x())) {
                var minY = min(a.y().intValue(), b.y().intValue());
                var maxY = max(a.y().intValue(), b.y().intValue());
                for (int y = minY; y <= maxY; y++) {
                    grid[y][a.x().intValue()] = '#';
                }
            } else if (a.y().equals(b.y())) {
                var minX = min(a.x().intValue(), b.x().intValue());
                var maxX = max(a.x().intValue(), b.x().intValue());
                for (int x = minX; x <= maxX; x++) {
                    grid[a.y().intValue()][x] = '#';
                }
            }
            a = b;
        }

        floodFill(grid);

        return allRectangles(redTiles)
                .stream()
                .filter(x -> isEnclosed(x, grid, xMap, yMap))
                .findFirst()
                .orElseThrow()
                .area();
    }

    private boolean isEnclosed(FloorRect rect, char[][] grid, Map<Long, Long> xMap, Map<Long, Long> yMap) {
        var a = rect.a();
        var b = rect.b();
        var x1 = (int) min(xMap.get(a.x()), xMap.get(b.x()));
        var x2 = (int) max(xMap.get(a.x()), xMap.get(b.x()));
        var y1 = (int) min(yMap.get(a.y()), yMap.get(b.y()));
        var y2 = (int) max(yMap.get(a.y()), yMap.get(b.y()));

        for (var x = x1; x <= x2; x++) {
            if (grid[y1][x] == '.' || grid[y2][x] == '.') return false;
        }
        for (var y = y1; y <= y2; y++) {
            if (grid[y][x1] == '.' || grid[y][x2] == '.') return false;
        }
        return true;
    }

    private void floodFill(char[][] grid) {
        var queue = new ArrayDeque<Vec2<Integer>>();
        queue.add(new Vec2<>(120, 7)); // some point inside found by inspecting input

        while (!queue.isEmpty()) {
            var point = queue.pop();
            if (grid[point.y()][point.x()] == '.') {
                grid[point.y()][point.x()] = '#';
                queue.add(new Vec2<>(point.x() + 1, point.y()));
                queue.add(new Vec2<>(point.x() - 1, point.y()));
                queue.add(new Vec2<>(point.x(), point.y() - 1));
                queue.add(new Vec2<>(point.x(), point.y() + 1));
            }
        }
    }

    private static void printGrid(char[][] grid) {
        for (var row : grid) {
            var strBuilder = new StringBuilder();
            for (var c : row) {
                strBuilder.append(c);
            }
            IO.println(strBuilder.toString());
        }
    }

    record FloorRect(Vec2<Long> a, Vec2<Long> b, long area) {}

    private static List<FloorRect> allRectangles(List<Vec2<Long>> redTiles) {
        var rects = new ArrayList<FloorRect>();
        for (var i = 0; i < redTiles.size(); i++) {
            var a = redTiles.get(i);
            for (var j = i + 1; j < redTiles.size(); j++) {
                var b = redTiles.get(j);
                var area = calculateArea(a, b);
                rects.add(new FloorRect(a, b, area));
            }
        }
        rects.sort(Comparator.comparing(FloorRect::area).reversed());
        return rects;
    }

    private static long calculateArea(Vec2<Long> a, Vec2<Long> b) {
        var w = Math.abs(b.x() - a.x()) + 1;
        var h = Math.abs(b.y() - a.y()) + 1;
        return w * h;
    }
}
