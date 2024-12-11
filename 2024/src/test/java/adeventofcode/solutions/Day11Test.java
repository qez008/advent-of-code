package adeventofcode.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day11Test {

    @Test
    void sampleOne() {
        assertEquals(55312L, new Day11("src/test/resources/input/day11-sample.txt").one());
    }

    @Test
    void one() {
        assertEquals(202019L, new Day11("src/test/resources/input/day11.txt").one());
    }

    @Test
    void two() {
        assertEquals(239321955280205L, new Day11("src/test/resources/input/day11.txt").two());
    }

}