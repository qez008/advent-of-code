package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class Day23Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var connections = Files
                .lines(Path.of(input))
                .map(line -> line.split("-"))
                .map(split -> new Day23.Connection(split[0], split[1]))
                .toList();
        return new Day23(connections);
    }

    @Test
    void example1() {
        assertEquals(7, sample().part1());
    }

    @Test
    void one() {
        assertEquals(1098, real().part1());
    }

    @Test
    void example2() {
        assertEquals("co,de,ka,ta", sample().part2());
    }

    @Test
    void two() {
        assertEquals("ar,ep,ih,ju,jx,le,ol,pk,pm,pp,xf,yu,zg", real().part2());
    }
}