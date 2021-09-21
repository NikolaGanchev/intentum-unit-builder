package main.utils;

import java.io.IOException;
import java.io.OutputStream;

public class StringBuilderOutputStream extends OutputStream {
    protected StringBuilder stringBuilder;

    public StringBuilderOutputStream(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void write(int character) throws IOException {
        this.stringBuilder.append((char) character);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
