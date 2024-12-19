package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Day19Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var lines = Files.readAllLines(Path.of(input));

        var patterns = Arrays.asList(lines.getFirst().split(", "));
        var designs = lines.subList(2, lines.size());

        return new Day19(patterns, designs);
    }

    @Test
    void example1() {
        assertEquals(6L, sample().part1());
    }

    @Test
    void one() {
        assertEquals(236L, real().part1());
    }

    @Test
    void example2() {
        assertEquals(16L, sample().part2());
    }

    @Test
    void two() {
        assertEquals(643685981770598L, real().part2());
    }
}