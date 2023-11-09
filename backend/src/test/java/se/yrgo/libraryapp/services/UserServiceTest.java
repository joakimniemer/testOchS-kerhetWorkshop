package se.yrgo.libraryapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;

    final PasswordEncoder encoder = org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();

    @Test
    void correctLogin() {
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final String realname = "bosse";
        final String password = "password";
        final String passwordHash = password;
        final User user = new User(id, username, realname, passwordHash);
        when(userDao.get(username)).thenReturn(Optional.of(user));
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }

    @Test
    void loginUserDontExist() {
        final String username = "testuser";
        final String password = "password";
        UserService userService = new UserService(userDao, encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.empty());
    }

    @Test
    void loginIncorrectPassword() {
        //Långsamt test pga användning av Argon2PasswordEncoder
        //Bör detta testas här?
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final String realname = "bosse";
        final String password = "password";
        final String passwordHash = password;
        final User user = new User(id, username, realname, passwordHash);
        when(userDao.get(username)).thenReturn(Optional.of(user));
        Argon2PasswordEncoder argonEncoder = new Argon2PasswordEncoder();
        UserService userService = new UserService(userDao, argonEncoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.empty());
    }

    @Test
    void addBackslashToNamesWithSingleQuotes() {
        UserService userService = new UserService(userDao, encoder);
        final String realname = "O'Toole";
        String cleanName = userService.addBackslashToNamesWithSingleQuotes(realname);

        assertThat(cleanName).isEqualTo("O\\'Toole");
    }


    @Test
    void registerSucceeded() {
        UserService userService = new UserService(userDao, encoder);
        final String name = "testuser";
        final String realname = "bosse";
        final String password = "password";
        Argon2PasswordEncoder encoderArgon2 = new Argon2PasswordEncoder();
        final String passwordHash = encoderArgon2.encode(password);
        when(userDao.register(name, realname, passwordHash)).thenReturn(true, false);
        boolean result = userService.registerUser(name, realname, passwordHash);

        //Kollar så userService.registerUser retunerar true (användaren kunde skapas).
        //Kollar så encoding av lösenord fungerar.
        assertThat(result).isTrue();
        assertThat(encoderArgon2.matches(password, passwordHash)).isTrue();
    }

    @Test
    void registerFailed() {
        UserService userService = new UserService(userDao, encoder);
        final String name = "testuser";
        final String realname = "bosse";
        final String password = "password";
        Argon2PasswordEncoder encoderArgon2 = new Argon2PasswordEncoder();
        final String passwordHash = encoderArgon2.encode(password);
        when(userDao.register(name, realname, passwordHash)).thenReturn(false);
        boolean result = userService.registerUser(name, realname, passwordHash);

        //Kollar så userService.registerUser retunerar false (användaren kunde inte skapas).
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"kim", "joakim", " haha ", "ha!ha!"})
    void nameIsAvailable(String username) {
        UserService userService = new UserService(userDao, encoder);
        when(userDao.isNameAvailable(username)).thenReturn(true);
        boolean result = userService.isNameAvailable(username);

        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"xx", " x ", "!#", "a ! ", "     "})
    @EmptySource
    void nameIsNotAvailable(String username) {
        //Kontrollerar att namn != null eller kortaren än 3 tecken (efter att alla whitespace är borttagna)
        //Ska aldrig komma till userDao.isNameAvailable, gör den det retuneras true och testet failar.
        //Använt lenient för att inte få UnnecessaryStubbingException, rimligt? Bättre lösning?
        UserService userService = new UserService(userDao, encoder);
        lenient().when(userDao.isNameAvailable(username)).thenReturn(true);
        boolean result = userService.isNameAvailable(username);

        assertThat(result).isFalse();
    }


}