package builder;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;

public class PrintableResult {
    private final String[] beginning;
    private final Document document;
    private final String[] ending;

    public PrintableResult(String[] beginning, Document document, String[] ending) {
        this.beginning = beginning;
        this.document = document;
        this.ending = ending;
    }

    public void prettyPrint() {
        printStringArray(beginning);

        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(System.out, format);
            writer.write(document);
        }
        catch (IOException e) {
            System.err.print("Unable to print because of the following error:");
            e.printStackTrace();
        }

        printStringArray(ending);
    }

    public void compactPrint() {
        printStringArray(beginning);

        try {
            OutputFormat format = OutputFormat.createCompactFormat();
            XMLWriter writer = new XMLWriter(System.out, format);
            writer.write(document);
            writer.close();
        }
        catch (IOException e) {
            System.err.print("Unable to print because of the following error:");
            e.printStackTrace();
        }

        printStringArray(ending);
    }

    private void printStringArray(String[] arrayToPrint) {
        for (String str : arrayToPrint) {
            System.out.print(str);
        }
    }
}
