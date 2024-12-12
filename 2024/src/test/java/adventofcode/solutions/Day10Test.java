package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test implements AocTest {

    @Test
    void samplePart1() {
        assertEquals(36, sample().part1());
    }

    @Test
    void part1() {
        assertEquals(737, real().part1());
    }

    @Test
    void samplePart2() {
        assertEquals(81, sample().part2());
    }

    @Test
    void part2() {
        assertEquals(1619, real().part2());
    }

    @Override
    public Solution parse(String input) throws Exception {
        var topologyMap = Files
                .lines(Path.of(input))
                .map(line -> Arrays.stream(line.split("")).map(Integer::parseInt).toList())
                .toList();
        return new Day10(topologyMap);
    }
}