package tokenizer;

import builder.Identifiers;
import tokenizer.Token;

import java.util.ArrayList;
import java.util.Iterator;

public class Tokenizer {
    private final ArrayList<String> tokenStrings;
    private ArrayList<Token> tokens;
    private final ArrayList<String> sanitizedTokenStrings;

    public Tokenizer() {
        this.tokenStrings = new ArrayList<>();
        this.sanitizedTokenStrings = new ArrayList<>();
    }

    public void addString(String token) {
        tokenStrings.add(token);
    }

    public void clear() {
        tokenStrings.clear();
        tokens.clear();
        sanitizedTokenStrings.clear();
    }

    public ArrayList<Token> tokenize() {
        tokens = new ArrayList<>();
        Token lastToken = null;

        for (String tokenString : tokenStrings) {
            Token token = new Token(tokenString);
            sanitizedTokenStrings.add(token.getToken());

            if (token.getIdentifier().startsWith(Identifiers.ANSWER) && lastToken != null) {
                lastToken.registerRelated(token);
                continue;
            }

            if (Identifiers.isRelatedToSwitchToken(token, lastToken)) {
                lastToken.registerRelated(token);
                continue;
            }

            lastToken = token;

            tokens.add(token);
        }

        return tokens;
    }

    public ArrayList<String> getSanitizedTokenStrings() {
        return this.sanitizedTokenStrings;
    }
}
