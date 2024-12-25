package adventofcode.solutions;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day25Test implements AocTest{

    @Override
    public Solution parse(String input) throws Exception {
        var locks = new ArrayList<int[]>();
        var keys = new ArrayList<int[]>();

        var lines = Files.readAllLines(Path.of(input));
        for (var i = 0; i < lines.size(); i+= 8) {
            var schematic = lines.subList(i, i + 7);
            var pins = new int[5];
            var isLock = schematic.getFirst().chars().mapToObj(c -> (char) c).allMatch(c -> c == '#');
            for (var j = 0; j < 5; j++) {
                for (var k = (isLock ? 1 : 0); k < (isLock ? 7 : 6); k++) {
                    if (schematic.get(k).charAt(j) == '#') {
                        pins[j]++;
                    }
                }
            }
            (isLock ? locks : keys).add(pins);
        }
        return new Day25(locks, keys);
    }

    @Test
    void example1() {
        assertEquals(3, sample().part1());
    }

    @Test
    void one() {
        assertEquals(3, real().part1());
    }
}