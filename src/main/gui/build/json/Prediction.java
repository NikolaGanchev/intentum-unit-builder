package main.gui.build.json;

public class Prediction {
    private String prediction;
    private int startingIndex;
    private int endingIndex;

    public Prediction(String prediction,int startingIndex, int endingIndex) {
        this.prediction = prediction;
        this.startingIndex = startingIndex;
        this.endingIndex = endingIndex;
    }

    public String getPrediction() {
        return prediction;
    }

    public int getStartingIndex() {
        return startingIndex;
    }

    public int getEndingIndex() {
        return endingIndex;
    }
}
