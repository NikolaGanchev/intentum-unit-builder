package main.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Documentation {
    private static String documentation;

    public static String getDocumentation() {
        if (documentation == null) {
            InputStream in = Documentation.class.getResourceAsStream("/resources/docs.html");
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                documentation = reader.lines().collect(Collectors.joining("\n"));
            }
        }

        return documentation;
    }
}
