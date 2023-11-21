package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

@Tag("integration")
@Testcontainers
public class UserDaoIntegrationTest {

    @Container
    private static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.29");

    private static DataSource ds;

    @BeforeAll
    static void setup() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mysql.getJdbcUrl());
        hikariConfig.setUsername(mysql.getUsername());
        hikariConfig.setPassword(mysql.getPassword());
        hikariConfig.setDriverClassName(mysql.getDriverClassName());
        UserDaoIntegrationTest.ds = new HikariDataSource(hikariConfig);
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.migrate();
    }

    // private static DataSource ds;

    // @BeforeAll
    // static void initDataSource() {
    // // this way we do not need to create a new datasource every time
    // final JdbcDataSource ds = new JdbcDataSource();
    // ds.setURL("jdbc:h2:mem:test");
    // UserDaoIntegrationTest.ds = ds;
    // }

    @Test
    void getUserByNameExisting() {
        final String username = "test";
        final UserId userId = UserId.of(1);
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(username);
        assertThat(maybeUser.isPresent()).isTrue();
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void getUserByNameNotExisting() {
        final String username = "joakim";
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(username);
        assertThat(maybeUser.isPresent()).isFalse();
        assertThat(maybeUser).isEqualTo(Optional.empty());
    }

    @Test
    void registerSucceded() {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        final String username = "Bosse";
        final String realname = "Bo";
        final String passwordHash = encoder.encode("password");
        UserDao userDao = new UserDao(ds);
        boolean registerSucceded = userDao.register(username, realname,
                passwordHash);

        assertThat(registerSucceded).isTrue();
        Optional<User> maybeUser = userDao.get(username);
        assertThat(maybeUser.isPresent()).isTrue();
    }

    @Test
    void registerFailedUsernameAlreadyExits() {
        // Borde man använda isNameAvailable i början av register och kasta ett fel
        // redan där?
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        final String username = "test";
        final String realname = "Bo";
        final String passwordHash = encoder.encode("password");
        UserDao userDao = new UserDao(ds);
        boolean registerSucceded = userDao.register(username, realname,
                passwordHash);

        assertThat(registerSucceded).isFalse();
    }

    @Test
    void nameIsAvailable() {
        final String username = "joakim";
        UserDao userDao = new UserDao(ds);
        boolean nameIsAvailable = userDao.isNameAvailable(username);
        assertThat(nameIsAvailable).isTrue();
    }

    @Test
    void nameIsNotAvailable() {
        final String username = "test";
        UserDao userDao = new UserDao(ds);
        boolean nameIsAvailable = userDao.isNameAvailable(username);
        assertThat(nameIsAvailable).isFalse();
    }
}