package org.rodnansol.asciidoctorj;

/**
 * Exception to be thrown when the Java Source code can not be extracted.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class JavaSourceCodeExtractionException extends RuntimeException {

    public JavaSourceCodeExtractionException(String message) {
        super(message);
    }

    public JavaSourceCodeExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
