package org.rodnansol.asciidoctorj;

import java.util.Objects;

/**
 * Class that wraps parameters for field extraction.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class ExtractFieldCommand extends ExtractCommand {

    private final String fieldName;

    public ExtractFieldCommand(ExtractCommand extractCommand, String fieldName) {
        super(extractCommand.getSourceCodePath(), extractCommand.getSpaceSize(), extractCommand.isWithJavaDoc(), extractCommand.getLineLength());
        this.fieldName = Objects.requireNonNull(fieldName, "fieldName is NULL");
    }

    /**
     * @param sourceCodePath Java source file's path.
     * @param fieldName      field name to extract.
     * @param spaceSize      space size for indentation.
     * @param withJavaDoc
     * @param lineLength
     */
    public ExtractFieldCommand(String sourceCodePath, String fieldName, int spaceSize, boolean withJavaDoc, int lineLength) {
        super(sourceCodePath, spaceSize, withJavaDoc, lineLength);
        this.fieldName = Objects.requireNonNull(fieldName, "fieldName is NULL");
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "ExtractFieldCommand{" +
            "fieldName='" + fieldName + '\'' +
            "} " + super.toString();
    }
}
