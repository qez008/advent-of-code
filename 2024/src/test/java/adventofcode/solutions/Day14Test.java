package adventofcode.solutions;

import adventofcode.util.IntVector2;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class Day14Test implements AocTest {

    @Test
    void example1() {
        assertEquals(12, sample().part1());
    }

    // too low:
    // - 45756
    // - 71290824
    @Test
    void one() {
        assertEquals(225521010L, real().part1());
    }

    @Test
    void two() {
        real().part2();
    }

    @Override
    public Solution parse(String input) throws Exception {
        var robots = Files
                .lines(Path.of(input))
                .map(line -> {
                    var pattern = Pattern.compile("p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)");
                    var matcher = pattern.matcher(line);
                    if (!matcher.matches()) {
                        throw new RuntimeException("Failed to parse input: " + input);
                    }
                    var start = new IntVector2(
                            Integer.parseInt(matcher.group(1)),
                            Integer.parseInt(matcher.group(2))
                    );
                    var velocity = new IntVector2(
                            Integer.parseInt(matcher.group(3)),
                            Integer.parseInt(matcher.group(4))
                    );
//                    System.out.println(start + " " + velocity);
                    return new Day14.Robot(start, velocity);
                })
                .toList();
        return new Day14(robots);
    }

}