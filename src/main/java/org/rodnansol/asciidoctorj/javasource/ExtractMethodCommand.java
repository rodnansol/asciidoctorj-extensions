package org.rodnansol.asciidoctorj.javasource;

import java.util.Arrays;
import java.util.Objects;

/**
 * Class that wraps parameters for method extraction.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class ExtractMethodCommand extends ExtractCommand {


    private final String methodName;
    private final String[] paramTypes;

    public ExtractMethodCommand(ExtractCommand extractCommand, String methodName,String... paramTypes) {
        super(extractCommand.getSourceCodePath(), extractCommand.getSpaceSize(), extractCommand.isWithJavaDoc(), extractCommand.getLineLength());
        this.methodName = Objects.requireNonNull(methodName, "methodName is NULL");
        this.paramTypes = paramTypes;
    }

    /**
     * @param sourceCodePath Java source file's path.
     * @param spaceSize      space size for indentation.
     * @param methodName     method's name that should be extracted from the source code.
     * @param withJavaDoc    if the JavaDoc should be included or not.
     * @param lineLength     maximum length of the line.
     * @param paramTypes     list of the method argument types in String values.
     */
    public ExtractMethodCommand(String sourceCodePath, int spaceSize, String methodName, boolean withJavaDoc, int lineLength, String... paramTypes) {
        super(sourceCodePath, spaceSize, withJavaDoc, lineLength);
        this.methodName = Objects.requireNonNull(methodName, "methodName is NULL");
        this.paramTypes = paramTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public String[] getParamTypes() {
        return paramTypes;
    }

    @Override
    public String toString() {
        return "ExtractMethodCommand{" +
            "methodName='" + methodName + '\'' +
            ", paramTypes=" + Arrays.toString(paramTypes) +
            "} " + super.toString();
    }
}
