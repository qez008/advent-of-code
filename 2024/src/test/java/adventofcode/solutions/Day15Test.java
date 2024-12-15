package adventofcode.solutions;

import adventofcode.util.IntVector2;
import com.google.common.base.Joiner;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day15Test implements AocTest {

    private Solution parsePartOne(String input) throws Exception {
        List<String> lines = Files.readAllLines(Path.of(input));
        int split = lines.indexOf("");

        List<String> mapData = lines.subList(0, split);

        var boxes = new HashSet<IntVector2>();
        var walls = new HashSet<IntVector2>();
        IntVector2 position = null;
        for (var y = 0; y < mapData.size(); y++) {
            var line = mapData.get(y).split("");
            for (var x = 0; x < mapData.size(); x++) {
                var cell = line[x];
                if (cell.equals("#")) {
                    walls.add(new IntVector2(x, y));
                } else if (cell.equals("O")) {
                    boxes.add(new IntVector2(x, y));
                } else if (cell.equals("@")) {
                    position = new IntVector2(x, y);
                }
            }
        }

        String instructions = Joiner.on("").join(lines.subList(split + 1, lines.size()));

        return new Day15(position, boxes, walls, instructions);
    }


    @Override
    public Solution parse(String input) throws Exception {
        List<String> lines = Files.readAllLines(Path.of(input));
        int split = lines.indexOf("");

        List<String> mapData = lines.subList(0, split);

        var boxes = new HashSet<IntVector2>();
        var walls = new HashSet<IntVector2>();
        IntVector2 position = null;
        for (var y = 0; y < mapData.size(); y++) {
            var line = mapData.get(y).split("");
            for (var x = 0; x < line.length; x++) {
                var cell = line[x];
                switch (cell) {
                    case "#" -> {
                        walls.add(new IntVector2(x * 2, y));
                        walls.add(new IntVector2(x * 2 + 1, y));
                    }
                    case "O" -> {
                        boxes.add(new IntVector2(x * 2, y));
//                        boxes.add(new IntVector2(x * 2 + 1, y));
                    }
                    case "@" -> position = new IntVector2(x * 2, y);
                }
            }
        }

        String instructions = Joiner.on("").join(lines.subList(split + 1, lines.size()));

        return new Day15(position, boxes, walls, instructions);
    }

    @Test
    void example1() {
        assertEquals(10092L, sample().part1());
    }

    @Test
    void one() {
        assertEquals(1568399L, real().part1());
    }

    @Test
    void example2() {
        assertEquals(9021L, sample().part2());
    }

    @Test
    void two() {
        assertEquals(1575877L, real().part2());
    }
}