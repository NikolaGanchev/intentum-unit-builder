import builder.*;
import tokenizer.Token;
import tokenizer.Tokenizer;
import transformers.DocumentToPrettyStringTransformer;
import transformers.ResultTransformer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

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
        ImportManager importManager = new ImportManager();
        importManager.addExpectedImports();

        Scanner in = new Scanner(System.in);
        String lessonToken = in.nextLine();

        Builder builder = new Builder(lessonToken, tokens, lockManager, importManager);

        BuildResult result = builder.build(new DocumentToPrettyStringTransformer());

        ResultTransformer resultTransformer = new ResultTransformer();

        System.out.println(resultTransformer.transform(result.getResult()));
    }
}
