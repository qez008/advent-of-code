package adeventofcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day3Test {

    private final Day3 day = new Day3("src/test/resources/input/day3.txt");

    @Test
    void one() {
        System.out.println("1) " + day.one());
    }

    @Test
    void two() {
        System.out.println("2) " + day.two());
    }

}