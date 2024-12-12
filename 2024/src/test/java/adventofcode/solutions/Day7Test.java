package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test implements AocTest {

    @Test
    void oneSample() {
        assertEquals(3749L, sample().part1());
    }

    @Test
    void one() {
        assertEquals(5540634308362L, real().part1());
    }

    @Test
    void twoSample() {
        assertEquals(11387L, sample().part2());
    }

    @Test
    void two() {
        assertEquals(472290821152397L, real().part2());
    }

    @Override
    public Solution parse(String input) throws Exception {
        var equations = Files.lines(Path.of(input)).map(Day7::findEquation).toList();
        return new Day7(equations);
    }
}
