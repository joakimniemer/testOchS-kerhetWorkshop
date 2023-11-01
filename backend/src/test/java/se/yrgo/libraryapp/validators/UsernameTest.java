package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {"abcd", ".a@a", "JOAKIM", "joakim@hotmail.com", "joakim1", "joakim99"})
    void assertjCorrectUsername(String validNames) {
        boolean result = Username.validate(validNames);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "    ", ".as", "*asd"})
    @EmptySource
    void assertjIncoorectUsername(String incorrectNames) {
        boolean result = Username.validate(incorrectNames);
        assertThat(result).isFalse();
    }
}
