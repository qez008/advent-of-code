package adventofcode.solutions;

import adventofcode.util.IntVector2;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test implements AocTest {

    @Test
    void part1Sample() {
        assertEquals(14L, sample().part1());
    }

    @Test
    void part1() {
        assertEquals(247L, real().part1());
    }

    @Test
    void part2Sample() {
        assertEquals(34, sample().part2());
    }

    // Too low:
    // - 773
    @Test
    void part2() {
        assertEquals(861L, real().part2());
    }

    @Override
    public Solution parse(String input) throws Exception {
        var grid = Files
                .lines(Path.of(input))
                .map(line -> {
                    var list = line.chars().mapToObj(c -> (char) c).toList();
                    return new ArrayList<>(list);
                })
                .toList();
        return new Day8(grid);
    }
}