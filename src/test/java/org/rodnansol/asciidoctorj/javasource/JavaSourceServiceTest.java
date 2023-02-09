package org.rodnansol.asciidoctorj.javasource;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class JavaSourceServiceTest {

    protected static final String USER_SERVICE_CLASS = "src/test/resources/UserService.java";
    protected static final String TEST_INTERFACE_SOURCE = "src/test/resources/InterfaceTest.java";
    protected static final String TEST_ENUM_CLASS = "src/test/resources/TestEnum.java";

    JavaSourceService underTest = JavaSourceService.INSTANCE;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class ClassTests {
        @Test
        void getField_shouldReturnUnknown_whenNotFound() throws IOException {
            // Given
            // When
            String logger = underTest.getField(USER_SERVICE_CLASS, "NOT_EXIST");
            // Then
            assertThat(logger).isEqualTo("<UNKNOWN>");
        }

        @Test
        void getField_shouldReturnField_whenFound() throws IOException {
            // Given
            // When
            String logger = underTest.getField(USER_SERVICE_CLASS, "LOGGER");
            // Then
            assertThat(logger).isEqualTo("private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);");
        }

        @Test
        void getMethod_shouldReturnEmptyString_whenNotFound() throws IOException {
            // Given
            // When
            String logger = underTest.getMethod(USER_SERVICE_CLASS, "notExists");
            // Then
            assertThat(logger).isEmpty();
        }

        @MethodSource("getMethodArgs")
        @ParameterizedTest
        void getMethod_shouldReturnMethod_whenFound(String methodName, String expectedExtractedContent, String[] paramTypes) throws IOException {
            // Given
            // When
            String method = underTest.getMethod(USER_SERVICE_CLASS, methodName, paramTypes);
            // Then
            assertThat(method).isEqualTo(expectedExtractedContent);
        }

        Stream<Arguments> getMethodArgs() {
            return Stream.of(
                arguments("createUser","public final User createUser(String username, String password) {\n" +
                    "    LOGGER.info(\"Creating new user with username:[{}]\", username);\n" +
                    "    userRepository.save(new User(username, password));\n" +
                    "}", new String[]{"String","String"}),
                arguments("deleteUser","protected void deleteUser(String username) {\n" +
                    "    LOGGER.info(\"Deleting user with username:[{}]\", username);\n" +
                    "    userRepository.deleteByUsername(username);\n" +
                    "}", new String[]{"String"}),
                arguments("disableUser","void disableUser(User user) {\n" +
                    "    LOGGER.info(\"Disabling user with username:[{}]\", user.username);\n" +
                    "    if (user.enabled == true) {\n" +
                    "        user.enabled = false;\n" +
                    "        userRepository.save(user);\n" +
                    "    } else {\n" +
                    "        logUnableToDisableUser();\n" +
                    "    }\n" +
                    "}", new String[]{"User"}),
                arguments("logUnableToDisableUser","private static void logUnableToDisableUser() {\n" +
                    "    LOGGER.info(\"Unable to disable disabled user\");\n" +
                    "}", new String[]{})
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class InterfaceTests {
        @Test
        void getField_shouldReturnUnknown_whenNotFound() throws IOException {
            // Given
            // When
            String logger = underTest.getField(TEST_INTERFACE_SOURCE, "NOT_EXIST");
            // Then
            assertThat(logger).isEqualTo("<UNKNOWN>");
        }

        @Test
        void getField_shouldReturnField_whenFound() throws IOException {
            // Given
            // When
            String logger = underTest.getField(TEST_INTERFACE_SOURCE, "ERROR_CODE");
            // Then
            assertThat(logger).isEqualTo("public static final String ERROR_CODE;");
        }

        @Test
        void getMethod_shouldReturnEmptyString_whenNotFound() throws IOException {
            // Given
            // When
            String logger = underTest.getMethod(TEST_INTERFACE_SOURCE, "notExists");
            // Then
            assertThat(logger).isEmpty();
        }

        @MethodSource("getMethodArgs")
        @ParameterizedTest
        void getMethod_shouldReturnMethod_whenFound(String methodName, String expectedExtractedContent, String[] paramTypes) throws IOException {
            // Given
            // When
            String method = underTest.getMethod(TEST_INTERFACE_SOURCE, methodName, paramTypes);
            // Then
            assertThat(method).isEqualTo(expectedExtractedContent);
        }

        Stream<Arguments> getMethodArgs() {
            return Stream.of(
                arguments("createUser","User createUser(String username, String password);", new String[]{"String","String"}),
                arguments("deleteUser","void deleteUser(String username);", new String[]{"String"}),
                arguments("disableUser","void disableUser(User user);", new String[]{"User"})
            );
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class EnumTests {
        @Test
        void getField_shouldReturnUnknown_whenNotFound() throws IOException {
            // Given
            // When
            String logger = underTest.getField(TEST_ENUM_CLASS, "NOT_EXIST");
            // Then
            assertThat(logger).isEqualTo("<UNKNOWN>");
        }

        @Test
        void getField_shouldReturnField_whenFound() throws IOException {
            // Given
            // When
            String logger = underTest.getField(TEST_ENUM_CLASS, "FIRST");
            // Then
            assertThat(logger).isEqualTo("FIRST");
        }

        @Test
        void getMethod_shouldReturnEmptyString_whenNotFound() throws IOException {
            // Given
            // When
            String logger = underTest.getMethod(TEST_ENUM_CLASS, "notExists");
            // Then
            assertThat(logger).isEmpty();
        }
    }

}
