package builder.json;

public class TranslationPair {
    protected String key;
    protected String translation;

    public TranslationPair(String key, String translation) {
        this.key = key;
        this.translation = translation;
    }

    public String getKey() {
        return key;
    }

    public String getTranslation() {
        return translation;
    }
}
