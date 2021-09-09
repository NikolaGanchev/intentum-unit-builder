package tokenizer;

import builder.Identifiers;
import tokenizer.Token;

import java.util.ArrayList;
import java.util.Iterator;

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
        Token lastToken = null;

        for (String tokenString : tokenStrings) {
            Token token = new Token(tokenString);

            if (token.getIdentifier().startsWith(Identifiers.ANSWER) && lastToken != null) {
                lastToken.registerRelated(token);
                tokens.add(token);
                continue;
            }

            lastToken = token;

            tokens.add(token);
        }

        return tokens;
    }

}
