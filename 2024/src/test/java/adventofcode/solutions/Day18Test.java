package adventofcode.solutions;

import adventofcode.util.IntVector2;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Day18Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var lines = Files.readAllLines(Path.of(input));

        var size = Arrays.stream(lines.getFirst().split(",")).map(Integer::parseInt).toArray(Integer[]::new);

        var fallingBytes = lines.subList(2, lines.size())
                .stream()
                .map(line -> {
                    var numbers = Arrays.stream(line.split(",")).map(Integer::parseInt).toArray(Integer[]::new);
                    return new IntVector2(numbers[0], numbers[1]);
                })
                .toList();
        return new Day18(new IntVector2(size[0], size[1]), fallingBytes);
    }

    @Test
    void example1() {
        assertEquals(22, sample().part1());
    }

    @Test
    void one() {
        assertEquals(22, real().part1());
    }

    @Test
    void example2() {
        assertEquals(0, sample().part2());
    }

    @Test
    void two() {
        assertEquals(0, real().part2());
    }
}