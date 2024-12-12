package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day9Test {

    @Test
    void expandTest() {
        var input = "2333133121414131402";
        var result = Day9.expand(input);
        assertEquals("00...111...2...333.44.5555.6666.777.888899", Day9.joinList(result));
    }

    @Test
    void relocateTest() {
        var input = "2333133121414131402";
        var expanded = Day9.expand(input);
        var result = Day9.relocate(expanded);
        assertEquals("0099811188827773336446555566..............", Day9.joinList(result));
    }

    @Test
    void sampleOne() {
        assertEquals(1928L, Day9.one("src/test/resources/input/day9-sample.txt"));
    }

    // Too low:
    // - 1408072414 (Integer not big enough
    @Test
    void one() {
        assertEquals(6332189866718L, Day9.one("src/test/resources/input/day9.txt"));
    }

    @Test
    void sampleTwo() {
        assertEquals(2858L, Day9.two("src/test/resources/input/day9-sample.txt"));
    }

    // Too low:
    // - 5245546828410
    // - 4140838076018
    @Test
    void two() {
        assertEquals(6353648390778L, Day9.two("src/test/resources/input/day9.txt"));
    }
}