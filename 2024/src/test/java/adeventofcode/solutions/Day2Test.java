package adeventofcode.solutions;

import org.junit.jupiter.api.Test;

class Day2Test {

    private final Day2 day = new Day2("src/test/resources/input/day2-orb.txt");

    @Test
    void one() {
        System.out.println("1) " + day.one());
    }

    @Test
    void two() {
        System.out.println("2) " + day.two());
    }

}