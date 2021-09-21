package main.transformers;

import java.util.ArrayList;

public class StringArrayListToSingleLineTransformer implements Transformer<String, ArrayList<String>> {
    @Override
    public String transform(ArrayList<String> lines) {
        StringBuilder result = new StringBuilder();

        for (String line : lines) {
            result.append(line);
            result.append("\\n");
        }

        return result.toString();
    }
}
