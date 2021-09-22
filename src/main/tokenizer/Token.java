package main.tokenizer;

import java.util.ArrayList;
import java.util.Arrays;

public class Token {
    // Banned characters are ", ', : and whitespace
    private static final String BANNED_CHARACTERS_REGEX = "[\"|:| |']";
    private final String token;
    private int number;
    private String identifier;
    private ArrayList<Token> relatedTokens;

    public Token(String token) {
        this.token = this.normalize(token);
        this.extract();
    }

    private void extract() {
        this.number = this.extractNumber();

        // Remove leading letter and digits
        this.identifier = this.getToken()
                .substring(1)
                .replaceAll("\\d", "");
    }

    private int extractNumber() {
        // Remove leading letter
        String tempToken = this.getToken().substring(1);
        String number = "";

        // The loop continues until it reaches a letter
        // This fixes the problem that the number of "p1a1" would evaluate to 11 with the previous solution
        for (int i = 0; i < tempToken.length(); i++) {
            if (Character.isDigit(tempToken.charAt(i))) {
                number += tempToken.charAt(i);
            }
            else {
                break;
            }
        }

        return Integer.parseInt(number);
    }

    private String normalize(String token) {
        String normalized = token
                .strip()
                .toLowerCase()
                .replaceAll(BANNED_CHARACTERS_REGEX, "");

        return normalized;
    }

    public void registerRelated(Token ...relatedTokens) {
        if (this.relatedTokens == null) {
            this.relatedTokens = new ArrayList<>();
        }

        this.relatedTokens.addAll(Arrays.stream(relatedTokens).toList());
    }

    public boolean hasRelated() {
        return this.relatedTokens != null && this.relatedTokens.size() != 0;
    }

    public Token[] getRelated() {
        Token[] relatedTokensArray = new Token[this.relatedTokens.size()];

        return this.relatedTokens.toArray(relatedTokensArray);
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
