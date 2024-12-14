package adventofcode.solutions;

import adventofcode.util.IntVector2;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day13Test implements AocTest {

    @Test
    void sampleOne() {
        assertEquals(480, sample().part1());
    }

    // Too low:
    // - 10408
    @Test
    void one() {
        assertEquals(10408, real().part1());
    }

    @Override
    public Solution parse(String input) throws Exception {
        var machines = Lists
                .partition(Files.readAllLines(Path.of(input)), 4)
                .stream()
                .map(sublist -> {
                    System.out.println(Joiner.on("\n").join(sublist));
                    var buttonA = new Day13.Button(findButtonDir(sublist.get(0)), 3);
                    var buttonB = new Day13.Button(findButtonDir(sublist.get(1)), 1);
                    return new Day13.Machine(
                            buttonA,
                            buttonB,
                            findPrice(sublist.get(2))
                    );
                })
                .toList();
        return new Day13(machines);
    }

    private IntVector2 findButtonDir(String input) {
        var pattern = Pattern.compile("X\\+(\\d+), Y\\+(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            var x = Integer.parseInt(matcher.group(1));
            var y = Integer.parseInt(matcher.group(2));
            return new IntVector2(x, y);
        }
        throw new RuntimeException("failed to parse " + input);
    }

    private IntVector2 findPrice(String input) {
        var pattern = Pattern.compile("X=(\\d+), Y=(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            var x = Integer.parseInt(matcher.group(1));
            var y = Integer.parseInt(matcher.group(2));
            return new IntVector2(x, y);
        }
        throw new RuntimeException("failed to parse " + input);
    }
}