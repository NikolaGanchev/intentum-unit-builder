package builder;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import utils.StringBuilderOutputStream;

import java.io.IOException;

public class BuildResult {
    private final String[] strings;

    public BuildResult(String ...strings) {
        this.strings = strings;
    }

    public String getResult() {
        StringBuilder result = new StringBuilder();

        for (String str: strings) {
            result.append(str);
        }

        return result.toString();
    }

    public void print() {
        for (String str: strings) {
            System.out.print(str);
        }
    }

    @Override
    public String toString() {
        return getResult();
    }
}
