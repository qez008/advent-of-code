package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class Day12Test implements AocTest {

    @Test
    void samplePart1() {
        assertEquals(1930, sample().part1());
    }

    @Test
    void part1() {
        assertEquals(1363682, real().part1());
    }

    @Test
    void samplePart2() {
        assertEquals(1206, sample().part2());
    }

    @Test
    void part2() {
        assertEquals(787680, real().part2());
    }

    @Override
    public Solution parse(String input) throws Exception {
        var garden = Files
                .lines(Path.of(input))
                .map(line -> line.chars().mapToObj(x -> (char) x).toList())
                .toList();
        return new Day12(garden);
    }
}