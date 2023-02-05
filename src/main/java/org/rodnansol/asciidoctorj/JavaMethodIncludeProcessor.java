package org.rodnansol.asciidoctorj;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.extension.PreprocessorReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Extension class that is able to handle the <b>include::javasource</b> macro and can extract and include full Java methods and fields.
 * <p>
 * Usage:
 * <p>
 * <pre>
 * [source,java]
 * ----
 * include::javasource[source={docdir}/UserService.java,method='saveUser']
 * ----
 * </pre>
 * Available attributes:
 * <ul>
 *     <li><b>source</b>* - Path to the Java source file</li>
 *     <li><b>method</b> - Name of the <b>method</b> to extract</li>
 *     <li><b>field</b> - Name of the <b>field</b> to extract</li>
 *     <li><b>spaceSize</b> - Space size to format the code - default is 4</li>
 *     <li><b>withJavaDoc</b> - If the JavaDoc should be included or not - default is false</li>
 *     <li><b>lineLength</b> - Maximum length per line - default is 120</li>
 * </ul>
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class JavaMethodIncludeProcessor extends IncludeProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaMethodIncludeProcessor.class);

    private static final String MACRO_JAVASOURCE = "javasource";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_SPACE_SIZE = "spaceSize";
    private static final String KEY_WITH_JAVA_DOC = "withJavaDoc";
    private static final String KEY_LINE_LENGTH = "lineLength";

    private final List<CodeBlockProcessor> codeBlockProcessorList;

    public JavaMethodIncludeProcessor() {
        this(Arrays.asList(new JavaMethodCodeBlockProcessor(), new JavaFieldCodeBlockProcessor()));
    }

    public JavaMethodIncludeProcessor(List<CodeBlockProcessor> codeBlockProcessorList) {
        this.codeBlockProcessorList = codeBlockProcessorList;
    }

    @Override
    public boolean handles(String target) {
        return MACRO_JAVASOURCE.equals(target);
    }

    @Override
    public void process(Document document,
                        PreprocessorReader reader,
                        String target,
                        Map<String, Object> attributes) {

        String source = (String) attributes.get(KEY_SOURCE);
        if (source == null || source.isEmpty()) {
            throw new JavaSourceCodeExtractionException("'source' attribute is empty.");
        }
        int spaceSize = Integer.parseInt((String) attributes.getOrDefault(KEY_SPACE_SIZE, String.valueOf(JavaSourceHelper.DEFAULT_SPACE_SIZE)));
        int lineLength = Integer.parseInt((String) attributes.getOrDefault(KEY_LINE_LENGTH, String.valueOf(JavaSourceHelper.DEFAULT_LINE_LENGTH)));
        boolean withJavaDoc = Boolean.parseBoolean((String) attributes.getOrDefault(KEY_WITH_JAVA_DOC, "false"));
        ExtractCommand extractCommand = new ExtractCommand(source, spaceSize, withJavaDoc, lineLength);
        LOGGER.debug("Extracting items from source code with basic command:[{}]", extractCommand);
        codeBlockProcessorList.stream()
            .filter(codeBlockProcessor -> codeBlockProcessor.isActive(attributes))
            .findFirst()
            .ifPresent(codeBlockProcessor -> codeBlockProcessor.process(extractCommand, document, reader, target, attributes));

    }
}
