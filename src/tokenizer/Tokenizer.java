package tokenizer;

import tokenizer.Token;

import java.util.ArrayList;

public class Tokenizer {
    private ArrayList<String> tokenStrings;
    private ArrayList<Token> tokens;

    public Tokenizer() {
        this.tokenStrings = new ArrayList<>();
    }

    public void addString(String token) {
        tokenStrings.add(token);
    }

    public void clear() {
        tokenStrings.clear();
        tokens.clear();
    }

    public ArrayList<Token> tokenize() {
        tokens = new ArrayList<>();

        for (String tokenString : tokenStrings) {
            Token token = new Token(tokenString);
            tokens.add(token);
        }

        return tokens;
    }

}
