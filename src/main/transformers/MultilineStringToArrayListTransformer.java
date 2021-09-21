package main.transformers;

import java.util.ArrayList;
import java.util.List;

public class MultilineStringToArrayListTransformer implements Transformer<ArrayList<String>, String> {
    @Override
    public ArrayList<String> transform(String multilineString) {
        String[] strings = multilineString.split("\n");

        return new ArrayList<>(List.of(strings));
    }
}
