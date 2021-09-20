package builder.json;

public class TokenIterator {
    private int counter;
    private String currentToken;
    private static final String KEY_FORMAT = "p%d";
    private static final String S_TOKEN_KEY_FORMAT = "s%d";

    public TokenIterator() {
        counter = 1;
        currentToken = KEY_FORMAT.formatted(counter);
    }

    public TokenIterator(int first) {
        counter = first;
        currentToken = KEY_FORMAT.formatted(counter);
    }

    public String next() {
        return next(false);
    }

    public String next(boolean shouldBeSToken) {
        counter++;
        currentToken = shouldBeSToken? S_TOKEN_KEY_FORMAT.formatted(counter): KEY_FORMAT.formatted(counter);
        return currentToken;
    }

    public String getTokenString() {
        return currentToken;
    }

    public String getTokenStringAsSToken() {
        return S_TOKEN_KEY_FORMAT.formatted(counter);
    }
}
