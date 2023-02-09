package org.rodnansol.asciidoctorj.javasource;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.PreprocessorReader;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JavaSourceIncludeProcessorTest {

    @Mock
    Document document;
    @Mock
    PreprocessorReader reader;
    String target = "javasource";

    @InjectMocks
    JavaSourceIncludeProcessor underTest;

    @Test
    void handles_shouldReturnTrue_whenIncludeContainsJavasource() {
        // Given
        // When
        boolean handles = underTest.handles("javasource");
        // Then
        assertThat(handles).isTrue();
    }

    @Test
    void handles_shouldReturnFalse_whenIncludeDoesNotContainJavasource() {
        // Given
        // When
        boolean handles = underTest.handles("javas");
        // Then
        assertThat(handles).isFalse();
    }

    @ParameterizedTest
    @EmptySource
    void process_shouldThrowException_whenSourceIsNullOrEmpty(String source) {
        // Given
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("source", source);
        // When
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.process(document, reader, target, attributes))
            .isInstanceOf(JavaSourceCodeExtractionException.class);
        // Then
    }

    @Test
    void process_shouldCallFirstActiveCodeBlockProcessor_whenProcessorsAreAvailable() {
        // Given
        CodeBlockProcessor codeBlockProcessor = mock(CodeBlockProcessor.class);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("source", "src/test/resources/UserService.java");
        attributes.put("spaceSize", "2");
        attributes.put("lineLength", "100");
        attributes.put("withJavaDoc", "false");
        when(codeBlockProcessor.isActive(attributes)).thenReturn(true);

        // When
        underTest = new JavaSourceIncludeProcessor(List.of(codeBlockProcessor));
        underTest.process(document, reader, target, attributes);

        // Then
        ExtractCommand expectedExtractCommand = new ExtractCommand("src/test/resources/UserService.java", 2, false, 100);
        verify(codeBlockProcessor).isActive(attributes);
        verify(codeBlockProcessor).process(expectedExtractCommand, document, reader, target, attributes);
    }

}

