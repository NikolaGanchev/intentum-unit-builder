package builder;

import com.google.gson.JsonObject;
import tokenizer.Tokenizer;

public interface CreateEvent {
    void jsonCreated(JsonObject jsonObject, Tokenizer tokenizer);
}
