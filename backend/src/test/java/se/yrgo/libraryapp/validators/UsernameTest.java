package se.yrgo.libraryapp.validators;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {"abcd", ".a@a", "JOAKIM", "joakim@hotmail.com"})
    void assertjCorrectUsername(String validNames) {
        Username validator = new Username();
        boolean result = validator.validate(validNames);
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "    ", ".as", "*asd"})
    @EmptySource
    void assertjIncoorectUsername(String incorrectNames) {
        Username validator = new Username();
        boolean result = validator.validate(incorrectNames);
        assertThat(result).isFalse();
    }
}
