package org.rodnansol;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleModuleTest {


    @Test
    void name() {
        // Given
        // When
        int sum = new SingleModule().sum(1, 1);
        // Then
        Assertions.assertThat(sum).isEqualTo(2);
    }
}
