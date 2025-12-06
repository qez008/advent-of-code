package aoc.twentyfive.common;

import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@UtilityClass
public class SecretsReader {

    private final String SECRETS_FILE_PATH = "secrets.properties";

    public String readSessionCookie() {
        Properties secrets = new Properties();
        try (FileReader reader = new FileReader(SECRETS_FILE_PATH)) {
            secrets.load(reader);
            return secrets.getProperty("aoc.session.cookie");
        } catch (IOException e) {
            System.err.println("ERROR: Could not read secrets file at " + SECRETS_FILE_PATH);
            throw new RuntimeException(e);
        }
    }
}
