package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Day22Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var buyers = Files.lines(Path.of(input)).map(Integer::parseInt).toList();
        return new Day22(buyers);
    }

    @Test
    void example1() {
        var buyers = List.of(1, 10, 100, 2024);
        var day = new Day22(buyers);

        assertEquals(37327623L, day.part1());
    }

    @Test
    void one() {
        assertEquals(17965282217L, real().part1());
    }

    @Test
    void testFindSequences() {
        Day22.findSequences(1);
    }

    @Test
    void example2() {
        var buyers = List.of(1, 2, 3, 2024);
        var day = new Day22(buyers);

        assertEquals(23L, day.part2());
    }

    // too low:
    // 2148
    // too high:
    // 2156
    // 2164
    @Test
    void two() {
        assertEquals(2152L, real().part2());
    }
}