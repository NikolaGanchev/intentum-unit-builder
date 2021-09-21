package main.builder;

import com.google.gson.JsonObject;
import main.tokenizer.Tokenizer;

public interface CreateEvent {
    void jsonCreated(JsonObject jsonObject, Tokenizer tokenizer);
}
