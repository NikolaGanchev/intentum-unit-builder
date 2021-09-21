package main.gui.build.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationTextPredictionGenerator {
    public static Prediction generatePrediction(String text, boolean trim) {

        Pattern searchPattern = Pattern.compile("\n+");
        Matcher matcher = searchPattern.matcher(text);
        if (matcher.find()) {
            int predictionEnd = matcher.start();
            String predictionText = text.substring(0, predictionEnd + 1);
            return new Prediction(trim? predictionText.trim(): predictionText, 0, predictionEnd + 1);
        }
        else {
            return new Prediction("Няма валидни подсказвания", -1, -1);
        }
    }
}
