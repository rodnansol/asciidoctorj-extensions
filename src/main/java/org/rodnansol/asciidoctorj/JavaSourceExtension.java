package org.rodnansol.asciidoctorj;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.jruby.extension.spi.ExtensionRegistry;

/**
 * Extension registry.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
public class JavaSourceExtension implements ExtensionRegistry {

    @Override
    public void register(Asciidoctor asciidoctor) {
        asciidoctor.javaExtensionRegistry()
            .blockMacro(JavaSourceBlockMacroProcessor.class);
        asciidoctor.javaExtensionRegistry()
            .includeProcessor(JavaMethodIncludeProcessor.class);
    }

}
