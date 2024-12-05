package adeventofcode.solutions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day5Test {

    // Too high:
    // - 6176
    // Correct: 4766
    @Test
    void one() {
        var day = new Day5("src/test/resources/input/day5.txt");
        System.out.println("1) " + day.one());
    }

    // Too low:
    // - 6000
    // - 6033
    // Correct: 6257
    @Test
    void two() {
        var day = new Day5("src/test/resources/input/day5.txt");
        System.out.println("2) " + day.two());
    }

}