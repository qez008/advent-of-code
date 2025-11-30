package aoc.twentyfive.comon;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.lang.IO.println;

public class PuzzleClient {

    private final OkHttpClient client = new OkHttpClient();

    public List<String> getPuzzleInput(int year, int day) {
        var filePath = Paths.get("inputs/%dD%02d.txt".formatted(year, day));
        if (Files.exists(filePath)) {
            println("File already exists");
            return readInputFromFile(filePath);
        }
        var puzzleInput = performGetRequest(year, day);
        writeInputToFile(filePath, puzzleInput);
        return Arrays.asList(puzzleInput.split("\n"));
    }

    private String performGetRequest(int year, int day) {
        var url = "https://adventofcode.com/{year}/day/{day}/input"
                .replace("{year}", String.valueOf(year))
                .replace("{day}", String.valueOf(day));
        var request = new Request.Builder()
                .get()
                .url(url)
                .header("User-Agent", "Mozilla")
                .header("Cookie", "session=" + SecretsReader.readSessionCookie())
                .build();
        println(request);
        try (var response = client.newCall(request).execute()) {
            println(response);
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> readInputFromFile(Path filePath) {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeInputToFile(Path filePath, String puzzleInput) {
        try {
            var newFile = Files.createFile(filePath);
            Files.writeString(newFile, puzzleInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
