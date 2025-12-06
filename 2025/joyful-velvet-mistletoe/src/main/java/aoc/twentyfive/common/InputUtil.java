package aoc.twentyfive.common;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@UtilityClass
public class InputUtil {

    private static final PuzzleClient puzzleClient = new PuzzleClient();

    public List<String> getInput(int day) {
        return puzzleClient.getPuzzleInput(2025, day);
    }

    public List<String> getSample(int day) {
        try {
            return Files.readAllLines(Paths.get("inputs/D%02ds.txt".formatted(day)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
