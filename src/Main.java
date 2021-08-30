import builder.Builder;
import builder.LockManager;
import builder.PrintableResult;
import tokenizer.Token;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Path pathToInput = Path.of("src/input.txt");
        Tokenizer tokenizer = new Tokenizer();

        try {
            Files.lines(pathToInput).forEach(tokenizer::addString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Token> tokens = tokenizer.tokenize();
        LockManager lockManager = new LockManager();

        Builder builder = new Builder("l1", tokens, lockManager);

        PrintableResult result = builder.build();

        result.prettyPrint();
    }
}
