package adeventofcode;

import org.junit.jupiter.api.Test;

class Day1Test {

    private final Day1 day = new Day1("src/test/resources/day1.txt");

    @Test
    void one() {
        System.out.println("1) " + day.one());
    }

    @Test
    void two() {
        System.out.println("2) " + day.two());
    }
}