package org.rodnansol.asciidoctorj;

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

    boolean isActive(Map<String, Object> attributes);

    void process(ExtractCommand extractCommand,
                 Document document,
                 PreprocessorReader reader,
                 String target,
                 Map<String, Object> attributes);

}
