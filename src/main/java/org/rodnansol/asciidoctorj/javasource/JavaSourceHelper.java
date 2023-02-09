package org.rodnansol.asciidoctorj.javasource;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster._shade.org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster._shade.org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MemberSource;
import org.jboss.forge.roaster.model.source.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Helper class containing methods to deal with Java source code files.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
class JavaSourceHelper {

    static final int DEFAULT_SPACE_SIZE = 4;
    static final int DEFAULT_LINE_LENGTH = 120;
    private static final String UNKNOWN_VALUE = "<UNKNOWN>";
    private static final String MOCK_CLASS_NAME = "class Source";
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaSourceHelper.class);

    private JavaSourceHelper() {
    }

    /**
     * Reads and extracts a specific field from the given Java source.
     *
     * @param extractFieldCommand@return Line containing the requested field or &lt;UNKNOWN&gt; if not found.
     * @throws IOException when the source code can not be read.
     */
    static String getField(ExtractFieldCommand extractFieldCommand) throws IOException {
        Objects.requireNonNull(extractFieldCommand.getSourceCodePath(), "sourceCodePath is NULL");
        Objects.requireNonNull(extractFieldCommand.getFieldName(), "fieldName is NULL");
        LOGGER.debug("Extracting field with command:[{}]", extractFieldCommand);
        JavaClassSource javaClassSource = parseSourceCode(extractFieldCommand.getSourceCodePath());
        return Optional.ofNullable(javaClassSource.getField(extractFieldCommand.getFieldName()))
            .map(fieldSource -> setupMockClassAndFormatAndExtractMember(fieldSource, extractFieldCommand.getSpaceSize(), extractFieldCommand.isWithJavaDoc(), extractFieldCommand.getLineLength()))
            .orElseGet(() -> {
                LOGGER.warn("Unable to find field:[{}] in [{}], fallback being returned", extractFieldCommand.getFieldName(), extractFieldCommand.getSourceCodePath());
                return UNKNOWN_VALUE;
            }).trim();
    }

    /**
     * Reads and extracts a specific field from the given Java source.
     *
     * @param sourceCodePath Java source file's path.
     * @param fieldName      field name to extract.
     * @return Line containing the requested field or &lt;UNKNOWN&gt; if not found.
     * @throws IOException when the source code can not be read.
     */
    static String getField(String sourceCodePath, String fieldName) throws IOException {
        return getField(new ExtractFieldCommand(sourceCodePath, fieldName, DEFAULT_SPACE_SIZE, false, DEFAULT_LINE_LENGTH));
    }

    /**
     * Extracts the requested method from the given Java source code.
     *
     * @param sourceCodePath Java source file's path.
     * @param methodName     method's name that should be extracted from the source code.
     * @param paramTypes     list of the method argument types in String values.
     * @return if the method is found then the method's body with its signature, otherwise an empty string.
     * @throws IOException when the source code can not be read.
     */
    static String getMethod(String sourceCodePath, String methodName, String... paramTypes) throws IOException {
        return getMethod(new ExtractMethodCommand(sourceCodePath, DEFAULT_SPACE_SIZE, methodName, false, DEFAULT_LINE_LENGTH, paramTypes));
    }

    /**
     * Extracts the requested method from the given Java source code.
     *
     * @param extractMethodCommand@return if the method is found then the method's body with its signature, otherwise an empty string.
     * @throws IOException when the source code can not be read.
     */
    static String getMethod(ExtractMethodCommand extractMethodCommand) throws IOException {
        Objects.requireNonNull(extractMethodCommand.getSourceCodePath(), "sourceCodePath is NULL");
        Objects.requireNonNull(extractMethodCommand.getMethodName(), "methodName is NULL");
        LOGGER.debug("Extracting method with command:[{}]", extractMethodCommand);
        JavaClassSource javaClassSource = parseSourceCode(extractMethodCommand.getSourceCodePath());
        MethodSource<JavaClassSource> methodSource = javaClassSource.getMethods()
            .stream()
            .filter(javaClassSourceMethodSource -> javaClassSourceMethodSource.getName().equals(extractMethodCommand.getMethodName()))
            .findFirst()
            .orElseGet(() -> javaClassSource.getMethod(extractMethodCommand.getMethodName(), extractMethodCommand.getParamTypes()));
        if (methodSource == null) {
            return StringUtils.EMPTY;
        }
        return setupMockClassAndFormatAndExtractMember(methodSource, extractMethodCommand.getSpaceSize(), extractMethodCommand.isWithJavaDoc(), extractMethodCommand.getLineLength());
    }

    private static JavaClassSource parseSourceCode(String sourceCodePath) throws IOException {
        return Roaster.parse(JavaClassSource.class, new File(sourceCodePath));
    }

    private static String setupMockClassAndFormatAndExtractMember(MemberSource<?, ?> memberSource, int spaceSize, boolean withJavaDoc, int lineLength) {
        Properties formatterProperties = setupFormatterProperties(spaceSize, lineLength);
        if (!withJavaDoc) {
            memberSource.removeJavaDoc();
        }
        //It sets up a mock class that can be formatted and removes the unneccessary spaces and indentations.
        String javaCode = MOCK_CLASS_NAME + "{" + memberSource.toString() + "}";
        String formattedCode = Roaster.format(formatterProperties, javaCode)
            .substring((MOCK_CLASS_NAME + " {\n" + StringUtils.repeat(" ", spaceSize) + "").length());
        return formattedCode.substring(0, formattedCode.length() - 2)
            .replaceAll("\n" + StringUtils.repeat(" ", spaceSize), "\n");
    }

    private static Properties setupFormatterProperties(int spaceSize, int lineLength) {
        Properties formatterProperties = new Properties();
        formatterProperties.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, String.valueOf(spaceSize));
        formatterProperties.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, "space");
        formatterProperties.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, lineLength);
        return formatterProperties;
    }
}
