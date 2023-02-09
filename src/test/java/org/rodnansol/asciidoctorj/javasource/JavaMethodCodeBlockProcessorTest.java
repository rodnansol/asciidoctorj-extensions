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
class JavaMethodCodeBlockProcessorTest {

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
    JavaMethodCodeBlockProcessor underTest;


    @Test
    void isActive_shouldReturnTrue_whenAttributesContainMethodKey() {
        // Given
        // When
        boolean active = underTest.isActive(Map.of("method", "myMethod"));
        // Then
        assertThat(active).isTrue();
    }

    @Test
    void isActive_shouldReturnFalse_whenAttributesDoesNotContainMethodKey() {
        // Given
        // When
        boolean active = underTest.isActive(Map.of("no-method", "myMethod"));
        // Then
        assertThat(active).isFalse();
    }

    @Test
    void process_houldThrowException_whenMethodCanNotBeRead() throws IOException {
        // Given
        Map<String, Object> attributes = ofEntries(
            entry("method", "myMethod"),
            entry("types", "String, int")
        );
        when(extractCommand.getSourceCodePath()).thenReturn("source-path");
        when(javaSourceService.getMethod(new ExtractMethodCommand(extractCommand, "myMethod", "String, int"))).thenThrow(IOException.class);
        // When
        AssertionsForClassTypes.assertThatThrownBy(()->underTest.process(extractCommand, document, reader, target, attributes))
            .isInstanceOf(JavaSourceCodeExtractionException.class);

        // Then
    }

    @Test
    void process_shouldGetMethodAndUseReaderToAppendToDocument_whenJavaSourceServiceReturnsValidMethodData() throws IOException {
        // Given
        Map<String, Object> attributes = ofEntries(
            entry("method", "myMethod"),
            entry("types", "String, int")
        );
        when(extractCommand.getSourceCodePath()).thenReturn("source-path");
        when(javaSourceService.getMethod(new ExtractMethodCommand(extractCommand, "myMethod", "String, int"))).thenReturn("methodBody");
        // When
        underTest.process(extractCommand, document, reader, target, attributes);

        // Then
        verify(javaSourceService).getMethod(new ExtractMethodCommand(extractCommand, "myMethod", "String, int"));
        verify(reader).pushInclude(eq("methodBody"), eq(target), any(), eq(1), eq(attributes));
    }
}
