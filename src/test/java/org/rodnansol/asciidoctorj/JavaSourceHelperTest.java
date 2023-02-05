package org.rodnansol.asciidoctorj;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class JavaSourceHelperTest {

    @Test
    void getField_shouldReturnUnknown_whenNotFound() throws IOException {
        // Given
        // When
        String logger = JavaSourceHelper.getField("src/test/resources/UserService.java", "NOT_EXIST");
        // Then
        assertThat(logger).isEqualTo("<UNKNOWN>");
    }

    @Test
    void getField_shouldReturnField_whenFound() throws IOException {
        // Given
        // When
        String logger = JavaSourceHelper.getField("src/test/resources/UserService.java", "LOGGER");
        // Then
        assertThat(logger).isEqualTo("private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);");
    }

    @Test
    void getMethod_shouldReturnEmptyString_whenNotFound() throws IOException {
        // Given
        // When
        String logger = JavaSourceHelper.getMethod("src/test/resources/UserService.java", "notExists");
        // Then
        assertThat(logger).isEmpty();
    }

    @MethodSource("getMethodArgs")
    @ParameterizedTest
    void getMethod_shouldReturnMethod_whenFound(String methodName, String expectedExtractedContent, String[] paramTypes) throws IOException {
        // Given
        // When
        String method = JavaSourceHelper.getMethod("src/test/resources/UserService.java", methodName, paramTypes);
        // Then
        assertThat(method).isEqualTo(expectedExtractedContent);
    }

    static Stream<Arguments> getMethodArgs() {
        return Stream.of(
            arguments("createUser","public final User createUser(String username, String password) {\n" +
                "    LOGGER.info(\"Creating new user with username:[{}]\", username);\n" +
                "    userRepository.save(new User(username, password));\n" +
                "}",(Object) new String[]{"String","String"}),
            arguments("deleteUser","protected void deleteUser(String username) {\n" +
                "    LOGGER.info(\"Deleting user with username:[{}]\", username);\n" +
                "    userRepository.deleteByUsername(username);\n" +
                "}",(Object) new String[]{"String"}),
            arguments("disableUser","void disableUser(User user) {\n" +
                "    LOGGER.info(\"Disabling user with username:[{}]\", user.username);\n" +
                "    if (user.enabled == true) {\n" +
                "        user.enabled = false;\n" +
                "        userRepository.save(user);\n" +
                "    } else {\n" +
                "        logUnableToDisableUser();\n" +
                "    }\n" +
                "}",(Object) new String[]{"User"}),
            arguments("logUnableToDisableUser","private static void logUnableToDisableUser() {\n" +
                "    LOGGER.info(\"Unable to disable disabled user\");\n" +
                "}", new String[]{})
        );
    }
}
