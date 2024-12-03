package adeventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class Util {

    private Util() {}

    public static void fileForEach(String input, Consumer<String> task) {
        try (BufferedReader reader = Files.newBufferedReader(Path.of(input))) {
            while (reader.readLine() instanceof String line) {
                task.accept(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
