package main.gui.build.json.gui;

import main.builder.json.TranslationPair;
import com.google.gson.JsonObject;
import main.json.Prediction;
import main.tokenizer.Tokenizer;

public class JsonModel {
    private String currentKey;
    private String currentDocument;
    private boolean stopIncrementing;
    private Prediction prediction;

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
}
