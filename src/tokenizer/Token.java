package tokenizer;

public class Token {
    // Banned characters are ", ', : and whitespace
    private static final String BANNED_CHARACTERS_REGEX = "[\"|:| |']";
    private final String token;
    private int number;
    private String identifier;

    public Token(String token) {
        this.token = this.normalize(token);
        this.extract();
    }

    private void extract() {
        // Remove all non-digits to remain with only the number
        // Then convert to int
        this.number = Integer.parseInt(this.getToken().replaceAll("\\D+",""));

        // Remove leading "p" and digits
        this.identifier = this.getToken()
                .substring(1)
                .replaceAll("\\d", "");
    }

    private String normalize(String token) {
        String normalized = token
                .strip()
                .toLowerCase()
                .replaceAll(BANNED_CHARACTERS_REGEX, "");

        return normalized;
    }

    public int getNumber() {
        return number;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getToken() {
        return token;
    }
}
