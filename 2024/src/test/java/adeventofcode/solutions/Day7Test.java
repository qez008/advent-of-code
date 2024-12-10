package adeventofcode.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {

    @Test
    void oneSample() {
        var day = new Day7("src/test/resources/input/day7-sample.txt");
        assertEquals(3749L, day.one());
    }

    @Test
    void one() {
        var day = new Day7("src/test/resources/input/day7.txt");
        assertEquals(5540634308362L, day.one());
    }

    @Test
    void twoSample() {
        var day = new Day7("src/test/resources/input/day7-sample.txt");
        assertEquals(11387L, day.two());
    }

    @Test
    void two() {
        var day = new Day7("src/test/resources/input/day7.txt");
        assertEquals(472290821152397L, day.two());
    }
}
