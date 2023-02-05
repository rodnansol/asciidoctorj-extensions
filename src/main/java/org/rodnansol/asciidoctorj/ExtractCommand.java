package org.rodnansol.asciidoctorj;

import java.util.Objects;

/**
 * Class that wraps an extraction command.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class ExtractCommand {

    private final String sourceCodePath;
    private final int spaceSize;
    private final boolean withJavaDoc;
    private final int lineLength;

    public ExtractCommand(String sourceCodePath, int spaceSize, boolean withJavaDoc, int lineLength) {
        this.sourceCodePath = Objects.requireNonNull(sourceCodePath, "sourceCodePath is NULL");
        this.spaceSize = spaceSize;
        this.withJavaDoc = withJavaDoc;
        this.lineLength = lineLength;
    }

    public String getSourceCodePath() {
        return sourceCodePath;
    }

    public int getSpaceSize() {
        return spaceSize;
    }

    public boolean isWithJavaDoc() {
        return withJavaDoc;
    }

    @Override
    public String toString() {
        return "ExtractCommand{" +
            "sourceCodePath='" + sourceCodePath + '\'' +
            ", spaceSize=" + spaceSize +
            ", withJavaDoc=" + withJavaDoc +
            ", lineLength=" + lineLength +
            '}';
    }

    public int getLineLength() {
        return lineLength;
    }
}
