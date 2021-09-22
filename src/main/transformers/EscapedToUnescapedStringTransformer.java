package main.transformers;

public class EscapedToUnescapedStringTransformer implements  Transformer<String, String>{
    @Override
    public String transform(String objectToTransform) {
        return objectToTransform.replaceAll("\\\\n", "\\n");
    }
}
