package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {

    @ParameterizedTest
    @ValueSource(strings = {"joakim", "lars", "anna"})
    void realnameValidatorTestTrue(String inputName) {
        boolean isNameValid = RealName.validate(inputName);
        assertThat(isNameValid).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"fan", "danne", "helvete"})
    void realnameValidatorTestFalse(String inputName) {
        boolean isNameValid = RealName.validate(inputName);
        assertThat(isNameValid).isFalse();
    }

}
