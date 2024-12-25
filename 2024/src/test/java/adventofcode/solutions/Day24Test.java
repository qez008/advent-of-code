package adventofcode.solutions;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class Day24Test implements AocTest {

    @Override
    public Solution parse(String input) throws Exception {
        var lines = Files.readAllLines(Path.of(input));
        var indexSplit = lines.indexOf("");
        var splitA = lines.subList(0, indexSplit);
        var splitB = lines.subList(indexSplit + 1, lines.size());

        var data = new HashMap<String, Boolean>();
        for (var line : splitA) {
            var split = line.split(": ");
            data.put(split[0], split[1].equals("1"));
        }

        var gates = new ArrayList<Day24.Gate>();
        for (var line : splitB) {
            var pattern = Pattern.compile("(\\w+) (\\w+) (\\w+) -> (\\w+)");
            var matcher = pattern.matcher(line);
            if (matcher.find()) {
                gates.add(new Day24.Gate(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)));
            }
        }
        return new Day24(data, gates);
    }

    @Test
    void example1() {
        assertEquals(4L, sample().part1());
    }

    @Test
    void one() {
        assertEquals(53755311654662L, real().part1());
    }

    @Test
    void two() {
        assertEquals("dkr,ggk,hhh,htp,rhv,z05,z15,z20", real().part2());
    }

}