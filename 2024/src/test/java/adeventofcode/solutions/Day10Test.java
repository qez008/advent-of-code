package adeventofcode.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day10Test {

    @Test
    void sampleOne() {
        var day = new Day10("src/test/resources/input/day10-sample.txt");
        assertEquals(36, day.one());
    }

    @Test
    void one() {
        var day = new Day10("src/test/resources/input/day10.txt");
        assertEquals(737, day.one());
    }

    @Test
    void sampleTwo() {
        var day = new Day10("src/test/resources/input/day10-sample.txt");
        assertEquals(81, day.two());
    }

    @Test
    void two() {
        var day = new Day10("src/test/resources/input/day10.txt");
        assertEquals(1619, day.two());
    }

}