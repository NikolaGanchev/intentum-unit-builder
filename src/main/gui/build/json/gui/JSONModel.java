package main.gui.build.json.gui;

import main.builder.json.TranslationPair;
import com.google.gson.JsonObject;
import main.gui.build.json.Prediction;
import main.tokenizer.Tokenizer;

public class JSONModel {
    private String currentKey;
    private String currentDocument;
    private Tokenizer tokens;
    private JsonObject jsonObject;
    private boolean stopIncrementing;
    private Prediction prediction;

    public JSONModel(JsonObject obj, Tokenizer tokens) {
        this.jsonObject = obj;
        this.tokens = tokens;
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentKey) {
        this.currentKey = currentKey;
    }

    public String getCurrentDocument() {
        return currentDocument;
    }

    public void setCurrentDocument(String currentDocument) {
        this.currentDocument = currentDocument;
    }

    public Tokenizer getTokens() {
        return tokens;
    }

    public void setTokens(Tokenizer tokens) {
        this.tokens = tokens;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject object) {
        this.jsonObject = object;
    }

    public boolean isStopIncrementing() {
        return stopIncrementing;
    }

    public void setStopIncrementing(boolean stopIncrementing) {
        this.stopIncrementing = stopIncrementing;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }

    public Prediction getPrediction() {
        return prediction;
    }

    public void addTranslationPair(TranslationPair translationPair, boolean sterialize) {
        this.tokens.addString(translationPair.getKey());

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
}
