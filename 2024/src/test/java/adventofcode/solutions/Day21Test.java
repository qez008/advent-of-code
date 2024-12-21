package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day21Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var codes = Files.readAllLines(Path.of(input));
        return new Day21(codes);
    }

    @Test
    void example1() {
        assertEquals(126384L, sample().part1());
    }

    @Test
    void one() {
        assertEquals(242484L, real().part1());
    }

    @Test
    void two() {
        assertEquals(294209504640384L, real().part2());
    }
}