package adeventofcode.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day8Test {

    @Test
    void oneSample() {
        var day = new Day8("src/test/resources/input/day8-sample.txt");
        assertEquals(14L, day.one());
    }

    @Test
    void one() {
        var day = new Day8("src/test/resources/input/day8.txt");
        assertEquals(247L, day.one());
    }

    @Test
    void twoSample() {
        var day = new Day8("src/test/resources/input/day8-sample.txt");
        assertEquals(34, day.two());
    }

    // Too low:
    // - 773
    @Test
    void two() {
        var day = new Day8("src/test/resources/input/day8.txt");
        assertEquals(861L, day.two());
    }

}