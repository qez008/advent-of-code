package adventofcode.solutions;

import org.junit.jupiter.api.Test;

class Day6Test {

    // Too low:
    // - 5100 (did not count the starting pos)
    // Correct: 5101
    @Test
    void one() {
        var day = new Day6("src/test/resources/input/day6.txt");
        System.out.println("1) " + day.one());
    }

    @Test
    void two() {
        var day = new Day6("src/test/resources/input/day6.txt");
        System.out.println("2) " + day.two());
    }
}