package aoc.twentyfive.comon;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@UtilityClass
public class InputUtil {

    public List<String> readFile(String name) {
        try {
            return Files.readAllLines(Paths.get("inputs/%s".formatted(name)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
