package gui.build.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationTextPredictionGenerator {
    public static Prediction generatePrediction(String text) {

        Pattern searchPattern = Pattern.compile("\n+");
        Matcher matcher = searchPattern.matcher(text);
        if (matcher.find()) {
            int predictionEnd = matcher.start();
            return new Prediction(text.substring(0, predictionEnd + 1), 0, predictionEnd + 1);
        }
        else {
            return new Prediction("Няма валидни подсказвания", -1, -1);
        }
    }
}
