package gui.build.json.gui;

import builder.json.TranslationPair;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import tokenizer.Tokenizer;

import java.util.ArrayList;

public class JSONModel {
    private String currentKey;
    private String currentDocument;
    private Tokenizer tokens;
    private JsonObject jsonObject;

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

    public void addTranslationPair(TranslationPair translationPair, boolean sterialize) {
        this.tokens.addString(translationPair.getKey());

        if (sterialize) {
            this.jsonObject.addProperty(translationPair.getKey(),
                    translationPair.getTranslation().trim().replaceAll("\n", ""));
        }
        else {
            this.jsonObject.addProperty(translationPair.getKey(), translationPair.getTranslation());
        }
    }

    public void addTranslationPair(String key, String translation, boolean sterialize) {
        addTranslationPair(new TranslationPair(key, translation), sterialize);
    }
}