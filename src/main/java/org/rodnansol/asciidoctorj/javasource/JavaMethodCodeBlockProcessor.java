package org.rodnansol.asciidoctorj.javasource;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.PreprocessorReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Code block processor that extract the method from the given source path.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class JavaMethodCodeBlockProcessor implements CodeBlockProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaMethodCodeBlockProcessor.class);

    private static final String KEY_METHOD = "method";
    private static final String KEY_METHOD_PARAM_TYPES = "types";

    private final JavaSourceService javaSourceService;

    public JavaMethodCodeBlockProcessor(JavaSourceService javaSourceService) {
        this.javaSourceService = javaSourceService;
    }

    @Override
    public boolean isActive(Map<String, Object> attributes) {
        return attributes.containsKey(KEY_METHOD);
    }

    @Override
    public void process(ExtractCommand extractCommand, Document document, PreprocessorReader reader, String target, Map<String, Object> attributes) {
        String method = (String) attributes.get(KEY_METHOD);
        try {
            String paramTypeList = (String) attributes.get(KEY_METHOD_PARAM_TYPES);
            String[] paramTypes = paramTypeList != null ? paramTypeList.split(",") : null;
            LOGGER.info("Extracting method:[{}] with type list:[{}] from source code at path:[{}]", method, paramTypes, extractCommand.getSourceCodePath());
            String fullMethod = javaSourceService.getMethod(new ExtractMethodCommand(extractCommand, method, paramTypes));

            reader.pushInclude(
                fullMethod,
                target,
                new File(".").getAbsolutePath(),
                1,
                attributes);
        } catch (IOException e) {
            throw new JavaSourceCodeExtractionException("Unable to extract method with name:[" + method + "]", e);
        }
    }
}
