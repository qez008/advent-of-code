package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class Day17Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var lines = Files.readAllLines(Path.of(input));

        var a = readRegister(lines.get(0));
        var b = readRegister(lines.get(1));
        var c = readRegister(lines.get(2));

        var program = Arrays
                .stream(lines.get(4).substring(9).split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        return new Day17(new Day17.Registers(a, b, c), program);
    }

    private long readRegister(String line) {
        var pattern = Pattern.compile("Register [A-C]: (\\d+)");
        var matcher = pattern.matcher(line);
        if (matcher.find()) {
            return Long.parseLong(matcher.group(1));
        }
        throw new RuntimeException("Unable to parse register " + line);
    }

    @Test
    void example1() {
        assertEquals(0, sample().part1());
    }


    @Test
    void one() {
        assertEquals(0, real().part1());
    }

    @Test
    void two() {
        assertEquals(164540892147389L, real().part2());
    }
}