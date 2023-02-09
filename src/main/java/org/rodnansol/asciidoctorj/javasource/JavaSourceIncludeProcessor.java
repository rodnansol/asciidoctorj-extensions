package org.rodnansol.asciidoctorj.javasource;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.IncludeProcessor;
import org.asciidoctor.extension.PreprocessorReader;
import org.jboss.forge.roaster._shade.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
public class JavaSourceIncludeProcessor extends IncludeProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaSourceIncludeProcessor.class);
    private static final String MACRO_JAVASOURCE = "javasource";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_SPACE_SIZE = "spaceSize";
    private static final String KEY_WITH_JAVA_DOC = "withJavaDoc";
    private static final String KEY_LINE_LENGTH = "lineLength";
    private final List<CodeBlockProcessor> codeBlockProcessorList;

    public JavaSourceIncludeProcessor() {
        this(Arrays.asList(new JavaMethodCodeBlockProcessor(JavaSourceService.INSTANCE), new JavaFieldCodeBlockProcessor(JavaSourceService.INSTANCE)));
    }

    public JavaSourceIncludeProcessor(List<CodeBlockProcessor> codeBlockProcessorList) {
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
        String spaceSizeRawValue = (String) attributes.get(KEY_SPACE_SIZE);
        String keyLineLengthRaw = (String) attributes.get(KEY_LINE_LENGTH);
        int spaceSize = Integer.parseInt(StringUtils.isEmpty(spaceSizeRawValue) ? String.valueOf(JavaSourceService.DEFAULT_SPACE_SIZE) : spaceSizeRawValue);
        int lineLength = Integer.parseInt(StringUtils.isEmpty(keyLineLengthRaw) ? String.valueOf(JavaSourceService.DEFAULT_LINE_LENGTH) : keyLineLengthRaw);
        boolean withJavaDoc = Boolean.parseBoolean((String) attributes.getOrDefault(KEY_WITH_JAVA_DOC, "false"));
        ExtractCommand extractCommand = new ExtractCommand(source, spaceSize, withJavaDoc, lineLength);
        LOGGER.debug("Extracting items from source code with basic command:[{}]", extractCommand);
        try {
            codeBlockProcessorList.stream()
                .filter(codeBlockProcessor -> codeBlockProcessor.isActive(attributes))
                .findFirst()
                .ifPresentOrElse(processor -> processor.process(extractCommand, document, reader, target, attributes),
                    () -> LOGGER.warn("No processor was found to handle incoming request for command:[{}]", extractCommand));
        } catch (Exception e) {
            LOGGER.error("Error during creating source code block...", e);
            renderErrorMessage(reader, target, attributes, e);
        }

    }

    private void renderErrorMessage(PreprocessorReader reader, String target, Map<String, Object> attributes, Exception exception) {
        StringBuilder errorMessageBuilder = new StringBuilder()
            .append("Unable to render the requested block, please see the stack trace for more information.\n\n")
            .append("Error message: ").append(exception.getMessage()).append(" \n");
        if (exception.getCause() != null) {
            errorMessageBuilder.append("Original error message: ").append(exception.getCause().getMessage()).append(" \n");
        }
        reader.pushInclude(
            errorMessageBuilder.toString(),
            target,
            new File(".").getAbsolutePath(),
            1,
            attributes);
    }
}
