package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class Day20Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var track = Files.readAllLines(Path.of(input));
        return new Day20(track);
    }

    @Test
    void example1() {
        assertEquals(44, sample().part1());
    }

    // too high:
    // - 5589
    // - 4000
    // - 3000
    @Test
    void one() {
        assertEquals(1441, real().part1());
    }

    @Test
    void example2() {
        assertEquals(1442, sample().part2());
    }

    // Too low:
    // - 163237
    // - 164237
    // - 1010152
    @Test
    void two() {
        assertEquals(1021490, real().part2());
    }
}