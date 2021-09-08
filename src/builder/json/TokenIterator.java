package builder.json;

public class TokenIterator {
    private int counter;
    private String currentToken;
    private static final String KEY_FORMAT = "p%d";

    public TokenIterator() {
        counter = 1;
        currentToken = KEY_FORMAT.formatted(counter);
    }

    public TokenIterator(int first) {
        counter = first;
        currentToken = KEY_FORMAT.formatted(counter);
    }

    public String next() {
        counter++;
        currentToken = KEY_FORMAT.formatted(counter);
        return currentToken;
    }

    public String getTokenString() {
        return currentToken;
    }
}
