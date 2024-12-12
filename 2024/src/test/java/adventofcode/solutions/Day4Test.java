package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class Day4Test {

    @Test
    void one() throws IOException {
        var day = new Day4("src/test/resources/input/day4.txt");
        System.out.println("1) " + day.one());
    }

    @Test
    void two() throws IOException {
        var day = new Day4("src/test/resources/input/day4.txt");
        System.out.println("2) " + day.two());
    }

}