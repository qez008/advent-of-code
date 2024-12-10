package adeventofcode.solutions;

import adeventofcode.util.IntVector2;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

class Day8 implements Solution {

    private final List<ArrayList<Character>> grid;
    private final Map<Character, List<IntVector2>> antennaMap;

    Day8(String input) {
        try (var lines = Files.lines(Path.of(input))) {
            this.grid = lines
                    .map(line -> {
                        var list = line.chars().mapToObj(c -> (char) c).toList();
                        return new ArrayList<>(list);
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.antennaMap = IntVector2
                .indexStream(grid)
                .filter(i -> getValue(i) != '.')
                .collect(Collectors.groupingBy(this::getValue));

        System.out.println(antennaMap);
    }

    private Character getValue(IntVector2 index) {
        return grid.get(index.y()).get(index.x());
    }

    private Character setAntinode(IntVector2 index) {
        return grid.get(index.y()).set(index.x(), '#');
    }

    public long one() {
        Set<IntVector2> antinodePositions = new HashSet<>();
        for (var entry : antennaMap.entrySet()) {
            var antennas = entry.getValue();
            for (IntVector2 a : antennas) {
                for (IntVector2 b : antennas) {
                    if (a == b) {
                        continue;
                    }
                    IntVector2 direction = a.minus(b);
                    IntVector2 position = a;
                    position = position.plus(direction);
                    try {
                        setAntinode(position);
                        antinodePositions.add(position);
                    } catch (IndexOutOfBoundsException e) {
                        // do nothing
                    }
                }
            }
        }
        for (var row : grid) {
            System.out.println(Joiner.on(" ").join(row));
        }
        return antinodePositions.size();
    }

    public long two() {
        for (var entry : antennaMap.entrySet()) {
            var antennas = entry.getValue();
            for (IntVector2 a : antennas) {
                for (IntVector2 b : antennas) {
                    if (a == b) {
                        continue;
                    }
                    IntVector2 direction = a.minus(b);
                    for (var i = 0; i < Math.max(grid.size(), grid.getFirst().size()); i++) {
                        try {
                            IntVector2 antinode = a.plus(direction.times(i));
                            setAntinode(antinode);
                        } catch (IndexOutOfBoundsException e) {
                            break;
                        }
                    }
                }
            }
        }
        for (var row : grid) {
            System.out.println(Joiner.on(" ").join(row));
        }
        return grid.stream().flatMap(ls -> ls.stream().filter(x -> x == '#')).count();
    }
}
