package transformers;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import utils.StringBuilderOutputStream;

import java.io.IOException;

public class DocumentToCompactStringTransformer implements  Transformer<String, Document> {

    @Override
    public String transform(Document document) {
        StringBuilder documentStringBuilder = new StringBuilder();

        StringBuilderOutputStream os = new StringBuilderOutputStream(documentStringBuilder);

        try {
            OutputFormat format = OutputFormat.createCompactFormat();
            XMLWriter writer = new XMLWriter(os, format);
            writer.write(document);
            os.close();
        }
        catch (IOException e) {
            System.err.print("Unable to write because of the following error:");
            e.printStackTrace();
        }

        return documentStringBuilder.toString();
    }
}
