package org.rodnansol.asciidoctorj;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.PreprocessorReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Code block processor that extracts the field from the given source path.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class JavaFieldCodeBlockProcessor implements CodeBlockProcessor {

    private static final String KEY_FIELD = "field";

    @Override
    public boolean isActive(Map<String, Object> attributes) {
        return attributes.containsKey(KEY_FIELD);
    }

    @Override
    public void process(ExtractCommand extractCommand, Document document, PreprocessorReader reader, String target, Map<String, Object> attributes) {
        String field = (String) attributes.get(KEY_FIELD);
        if (field != null && !field.isEmpty()) {
            try {
                String fullField = JavaSourceHelper.getField(new ExtractFieldCommand(extractCommand, field));
                reader.pushInclude(
                    fullField,
                    target,
                    new File(".").getAbsolutePath(),
                    1,
                    attributes);
            } catch (IOException e) {
                throw new JavaSourceCodeExtractionException("Unable to extract field with name:[" + field + "]", e);
            }
        }
    }
}
