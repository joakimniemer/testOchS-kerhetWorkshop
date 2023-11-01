package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class UtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"apa apa", "apa", " bepacepa", "    ", "HA ha Haaa", "APAPAPA"})
    void removeNonAlphabeticLettersExceptWhitespaceFromStringTestIsTrue(String inputString) {
        String filteredString = Utils.onlyLettersAndWhitespace(inputString);

        boolean isAlphabeticOrWhitespace = filteredString.matches("[a-zA-Z\\s]+");
        boolean isLowerCase = filteredString.equals(filteredString.toLowerCase());
        boolean isSameLength = filteredString.length() == inputString.length();

        assertThat(isAlphabeticOrWhitespace && isLowerCase && isSameLength).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"@pa apa", "haha!", "ha ha! !", "joakim@hotmail.com", "abcd1234", "12345"})
    void removeNonAlphabeticLettersExceptWhitespaceFromStringTestIsFalse(String inputString) {
        String filteredString = Utils.onlyLettersAndWhitespace(inputString);

        boolean isAlphabeticOrWhitespace = filteredString.matches("[a-zA-Z\\s]+");
        boolean isLowerCase = filteredString.equals(filteredString.toLowerCase());
        boolean isSameLength = filteredString.length() == inputString.length();

        assertThat(isAlphabeticOrWhitespace && isLowerCase && isSameLength).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1337", "h4x", "h4x h4x"})
    void cleanAndUnLeetTestIsTrue(String inputString) {
        String controllString = "13456780";
        String cleanAndUnLeetedString = Utils.cleanAndUnLeet(inputString);
        String cleanAndUnLeetedControllString = Utils.cleanAndUnLeet(controllString);
        boolean isControllStringCorrect = cleanAndUnLeetedControllString.equals("leasgtbo");
        boolean isSameLength = cleanAndUnLeetedString.length() == inputString.length();

        assertThat(isSameLength && isControllStringCorrect).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1337 !", "h4x 22", "235555", "1 2 3 4 5 6 7 8 9 10"})
    void cleanAndUnLeetTestIsFalse(String inputString) {
        String controllString = "13456780";
        String cleanAndUnLeetedString = Utils.cleanAndUnLeet(inputString);
        String cleanAndUnLeetedControllString = Utils.cleanAndUnLeet(controllString);
        boolean isControllStringCorrect = cleanAndUnLeetedControllString.equals("leasgtbo");
        boolean isSameLength = cleanAndUnLeetedString.length() == inputString.length();

        assertThat(isControllStringCorrect).isTrue();
        assertThat(isSameLength).isFalse();
    }
}
