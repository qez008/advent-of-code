package adeventofcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Day2Test {

    private final Day2 day = new Day2("src/test/resources/day2.txt");

    @Test
    void one() {
        System.out.println("1) " + day.one());
    }

    @Test
    void two() {
        System.out.println("2) " + day.two());
    }

}