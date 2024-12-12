package adventofcode.solutions;

import adventofcode.util.AocUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

class Day3 {

    private final List<String> lines;

    Day3(String input) {
        this.lines = new ArrayList<>();
        AocUtil.fileForEach(input, lines::add);
    }

    int one() {
        return lines
                .stream()
                .map(string -> {
                    var pattern = Pattern.compile("(\\d+),(\\d+)");
                    var matcher = pattern.matcher(string);
                    if (matcher.find()) {
                        var x = Integer.parseInt(matcher.group(1));
                        var y = Integer.parseInt(matcher.group(2));
                        return x * y;
                    }
                    throw new RuntimeException("wtf");
                })
                .reduce(0, Integer::sum);
    }

    int two() {
        AtomicBoolean active = new AtomicBoolean(true);
        return lines
                .stream()
                .flatMap(line -> {
                    var expressions = new ArrayList<String>();
                    var pattern = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\(\\d+,\\d+\\)");
                    var matcher = pattern.matcher(line);

                    while (matcher.find()) {
                        expressions.add(matcher.group());
                    }
                    return expressions.stream();
                })
                .map(command -> switch (command) {
                    case "do()" -> {
                        active.getAndSet(true);
                        yield 0;
                    }
                    case "don't()" -> {
                        active.getAndSet(false);
                        yield 0;
                    }
                    default -> {
                        if (!active.get()) {
                            yield 0;
                        }
                        var pattern = Pattern.compile("(\\d+),(\\d+)");
                        var matcher = pattern.matcher(command);
                        if (matcher.find()) {
                            var x = Integer.parseInt(matcher.group(1));
                            var y = Integer.parseInt(matcher.group(2));
                            yield x * y;
                        }
                        throw new RuntimeException("wtf");
                    }
                })
                .reduce(0, Integer::sum);
    }
}
