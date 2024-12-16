package adventofcode.solutions;

import adventofcode.util.IntVector2;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day16Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var grid = Files
                .lines(Path.of(input))
                .map(line -> line.chars().mapToObj(c -> (char) c).toList())
                .toList();
        var start = IntVector2
                .indexStream(grid)
                .filter(v -> v.getValueFrom(grid) == 'S')
                .findFirst()
                .orElseThrow();
        var end = IntVector2
                .indexStream(grid)
                .filter(v -> v.getValueFrom(grid) == 'E')
                .findFirst()
                .orElseThrow();
        return new Day16(grid, start, end);
    }

    @Test
    void example1() {
        assertEquals(11048, sample().part1());
    }

    @Test
    void one() {
        assertEquals(98416, real().part1());
    }

    @Test
    void example2() {
        assertEquals(64, sample().part2());
    }

    // too low: 463, 464, 468
    @Test
    void two() {
        assertEquals(471, real().part2());
    }
}