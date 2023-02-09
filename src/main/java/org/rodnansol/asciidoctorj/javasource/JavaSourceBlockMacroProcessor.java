package org.rodnansol.asciidoctorj.javasource;

import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockMacroProcessor;
import org.asciidoctor.extension.Name;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is in BETA phase, please do not use this macro if not interested in it.
 *
 * @author nandorholozsnyak
 * @since 0.1.0
 */
@Name("javasource")
public class JavaSourceBlockMacroProcessor extends BlockMacroProcessor {

    private final JavaSourceService javaSourceService;

    public JavaSourceBlockMacroProcessor() {
        this.javaSourceService = JavaSourceService.INSTANCE;
    }

    @Override
    public Object process(StructuralNode parent, String target, Map<String, Object> attributes) {
        try {
            String method = javaSourceService.getMethod(target, (String) attributes.get("method"));
            String content = new StringBuilder()
                .append("<div class=\"listingblock\">\n")
                .append("<div class=\"content\">\n")
                .append("<pre class=\"rouge highlight\">\n")
                .append("<code data-lang=\"java\">\n")
                .append(method)
                .append("\n</code>")
                .append("\n</pre>")
                .append("\n</div>")
                .append("\n</div>")
                .toString();
            Map<String, Object> params = new HashMap<>();
            params.put("style", "source");
            return createBlock(parent, "pass", content, params);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
