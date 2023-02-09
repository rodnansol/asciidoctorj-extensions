package org.rodnansol.asciidoctorj;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.jruby.extension.spi.ExtensionRegistry;
import org.rodnansol.asciidoctorj.javasource.JavaSourceBlockMacroProcessor;
import org.rodnansol.asciidoctorj.javasource.JavaSourceIncludeProcessor;

/**
 * Extension registry.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class CustomExtensionRegistry implements ExtensionRegistry {

    @Override
    public void register(Asciidoctor asciidoctor) {
        asciidoctor.javaExtensionRegistry()
            .blockMacro(JavaSourceBlockMacroProcessor.class);
        asciidoctor.javaExtensionRegistry()
            .includeProcessor(JavaSourceIncludeProcessor.class);
    }

}
