package transformers;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import utils.StringBuilderOutputStream;

import java.io.IOException;

public interface Transformer<T, K> {

    T transform(K objectToTransform);
}
