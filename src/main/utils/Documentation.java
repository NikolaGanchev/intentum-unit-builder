package main.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Documentation {
    private static String documentation;

    public static String getDocumentation() {
        if (documentation == null) {
            try {
                documentation = Files.readString(Path.of("src/resources/docs.html"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return documentation;
    }
}
