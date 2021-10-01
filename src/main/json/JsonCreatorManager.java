package main.json;

import com.google.gson.JsonObject;
import main.builder.Identifiers;
import main.builder.json.TokenIterator;
import main.builder.json.TranslationPair;
import main.gui.build.last.exceptions.KeyAlreadyExistsException;
import main.tokenizer.Token;
import main.tokenizer.Tokenizer;

public class JsonCreatorManager {
    private Tokenizer tokenizer;
    private JsonObject jsonObject;
    private boolean isIncrementing;
    private TokenIterator tokenIterator;
    private BooleanChangeEvent booleanChangeEvent;

    public JsonCreatorManager(JsonObject jsonObject, Tokenizer tokenizer, TokenIterator tokenIterator) {
        this.jsonObject = jsonObject;
        this.tokenizer = tokenizer;
        this.tokenIterator = tokenIterator;
    }

    public void addTranslationPair(TranslationPair translationPair, boolean sterialize) {
        this.tokenizer.addString(translationPair.getKey());

        if (sterialize) {
            this.jsonObject.addProperty(translationPair.getKey(),
                    translationPair.getTranslation().trim().replaceAll("\n", ""));
        }
        else {
            this.jsonObject.addProperty(translationPair.getKey(), translationPair.getTranslation().trim());
        }
    }

    public void addTranslationPair(String key, String translation, boolean sterialize) {
        addTranslationPair(new TranslationPair(key, translation), sterialize);
    }

    public String getNextKey(Token token, boolean isManuallySet) throws KeyAlreadyExistsException {
        if (jsonObject.has(token.getToken())) {
            throw new KeyAlreadyExistsException();
        }

        String nextIdentifier = token.getIdentifier();

        // Don't increment if the identifier is a question or answer
        if (Identifiers.isQuestion(token) || token.getIdentifier().equals(Identifiers.ANSWER)) {
            nextIdentifier = Identifiers.ANSWER;
            setIncrementing(false);
        }
        else if (!isIncrementing() && !isManuallySet) {
            tokenIterator.next();
            setIncrementing(true);
        }

        if (!isIncrementing) {
            return tokenIterator.getTokenString() + nextIdentifier;
        }

        return tokenIterator
                .next(Identifiers.isPartOfSwitch(token)) + nextIdentifier;
    }

    public String getCurrentKey() {
        return tokenIterator.getTokenString();
    }

    public Prediction getPrediction(String stringToParse) {
        return TranslationTextPredictionGenerator.generatePrediction(stringToParse, true);
    }

    public void setIsIncrementingEvent(BooleanChangeEvent booleanChangeEvent) {
        this.booleanChangeEvent = booleanChangeEvent;
    }

    public void setIncrementing(boolean incrementing) {
        if (booleanChangeEvent != null) {
            booleanChangeEvent.onAction(isIncrementing, incrementing);
        }
        isIncrementing = incrementing;
    }

    public boolean isIncrementing() {
        return isIncrementing;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }
}
