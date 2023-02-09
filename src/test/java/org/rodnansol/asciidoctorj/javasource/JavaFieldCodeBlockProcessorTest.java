package org.rodnansol.asciidoctorj.javasource;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.PreprocessorReader;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JavaFieldCodeBlockProcessorTest {

    @Mock
    JavaSourceService javaSourceService;
    @Mock
    ExtractCommand extractCommand;
    @Mock
    Document document;
    @Mock
    PreprocessorReader reader;
    String target = "javasource";

    @InjectMocks
    JavaFieldCodeBlockProcessor underTest;


    @Test
    void isActive_shouldReturnTrue_whenAttributesContainMethodKey() {
        // Given
        // When
        boolean active = underTest.isActive(Map.of("field", "myField"));
        // Then
        assertThat(active).isTrue();
    }

    @Test
    void isActive_shouldReturnFalse_whenAttributesDoesNotContainMethodKey() {
        // Given
        // When
        boolean active = underTest.isActive(Map.of("no-field", "myField"));
        // Then
        assertThat(active).isFalse();
    }

    @Test
    void process_houldThrowException_whenFieldCanNotBeRead() throws IOException {
        // Given
        Map<String, Object> attributes = ofEntries(
            entry("field", "myField")
        );
        when(extractCommand.getSourceCodePath()).thenReturn("source-path");
        when(javaSourceService.getField(new ExtractFieldCommand(extractCommand, "myField"))).thenThrow(IOException.class);
        // When
        AssertionsForClassTypes.assertThatThrownBy(() -> underTest.process(extractCommand, document, reader, target, attributes))
            .isInstanceOf(JavaSourceCodeExtractionException.class);

        // Then
    }

    @Test
    void process_shouldGetMethodAndUseReaderToAppendToDocument_whenJavaSourceServiceReturnsValidMethodData() throws IOException {
        // Given
        Map<String, Object> attributes = ofEntries(
            entry("field", "myField")
        );
        when(extractCommand.getSourceCodePath()).thenReturn("source-path");
        when(javaSourceService.getField(new ExtractFieldCommand(extractCommand, "myField"))).thenReturn("fieldBody");
        // When
        underTest.process(extractCommand, document, reader, target, attributes);

        // Then
        verify(javaSourceService).getField(new ExtractFieldCommand(extractCommand, "myField"));
        verify(reader).pushInclude(eq("fieldBody"), eq(target), any(), eq(1), eq(attributes));
    }
}
