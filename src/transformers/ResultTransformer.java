package transformers;

public class ResultTransformer implements Transformer<String, String>{
    private static final String XML_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
    private static final String ENCODED_QUOTE = "&quot;";
    private static final String LEFT_TAG = "&lt;";
    private static final String RIGHT_TAG = "&gt;";

    @Override
    public String transform(String inputString) {
        String result = inputString
                .replace(XML_STRING, "")
                .replaceAll(ENCODED_QUOTE, "\"")
                .replaceAll(LEFT_TAG, "<")
                .replaceAll(RIGHT_TAG, ">");

        while (result.contains("\"{")) {
            int startIndex = result.indexOf("\"{");
            int endIndex = result.indexOf("}\"");
            if (startIndex < endIndex) {
                result = result.substring(0, startIndex)
                        + result.substring(startIndex + 1, endIndex + 1)
                        + result.substring(endIndex + 2);
            }
        }

        return result;
    }
}
