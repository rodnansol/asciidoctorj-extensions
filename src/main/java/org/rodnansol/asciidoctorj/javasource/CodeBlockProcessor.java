package org.rodnansol.asciidoctorj.javasource;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.PreprocessorReader;

import java.util.Map;

/**
 * Interface describes a block processor blueprint that can handle different attributes from the 'javasource' include macro.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public interface CodeBlockProcessor {

    /**
     * Checks if the current instance should be active for processing or not.
     *
     * @param attributes AsciiDoc attributes coming outside.
     * @return <b>true</b> if the processor must be active based on the attributes, otherwise <b>false</b>
     */
    boolean isActive(Map<String, Object> attributes);

    /**
     * Processes the incoming source code and creates and extra block into the requested document file.
     *
     * @param extractCommand base extract command.
     * @param document       AsciiDoc document instance.
     * @param reader         pre-processor reader.
     * @param target         target section.
     * @param attributes     AsciiDoc attributes.
     * @throws JavaSourceCodeExtractionException when the source code can not be read or transformed.
     */
    void process(ExtractCommand extractCommand,
                 Document document,
                 PreprocessorReader reader,
                 String target,
                 Map<String, Object> attributes);

}
