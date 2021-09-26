package main.transformers;

import java.util.ArrayList;

public class StringArrayListToSingleLineTransformer implements Transformer<String, ArrayList<String>> {
    @Override
    public String transform(ArrayList<String> lines) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < lines.size(); i++) {
            result.append(lines.get(i));
            // Don't append newline to last line
            if (i != lines.size() - 1) {
                result.append("\\n");
            }
        }

        return result.toString().trim();
    }
}
