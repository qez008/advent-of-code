package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day11Test implements AocTest {

    @Test
    void samplePart1() {
        assertEquals(55312, sample().part1());
    }

    @Test
    void part1() {
        assertEquals(202019, real().part1());
    }

    @Test
    void part2() {
        assertEquals(239321955280205L, real().part2());
    }

    @Override
    public Solution parse(String input) throws Exception {
        var initialLine = Arrays
                .stream(Files.readString(Path.of(input)).split(" "))
                .map(Long::parseLong)
                .toList();
        return new Day11(initialLine);
    }
}