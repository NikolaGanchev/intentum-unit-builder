import builder.*;
import tokenizer.Token;
import tokenizer.Tokenizer;
import transformers.DocumentToPrettyStringTransformer;
import transformers.ResultTransformer;
import transformers.StringArrayListToSingleLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static boolean multilineToSingleLine = false;

    public static void main(String[] args) {
        Path pathToInput = Path.of(multilineToSingleLine? "src/input_multiline_to_singleline.txt": "src/input.txt");

        Tokenizer tokenizer = new Tokenizer();

        try {
            // Check if there is a multiline to singleline request
            // That comes in handy when you have to put in code translations
            if (multilineToSingleLine) {
                ArrayList<String> lines = new ArrayList<>();
                Files.lines(pathToInput).forEach(lines::add);

                StringArrayListToSingleLine transformer = new StringArrayListToSingleLine();
                System.out.println(transformer.transform(lines));
                return;
            } // Else start lesson building process
            else {
                Files.lines(pathToInput).forEach(tokenizer::addString);
            }
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
