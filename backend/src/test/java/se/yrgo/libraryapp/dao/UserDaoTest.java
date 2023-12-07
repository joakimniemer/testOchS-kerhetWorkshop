package se.yrgo.libraryapp.dao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.sql.*;
import java.util.Optional;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.User;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserDaoTest {
    @Mock
    private DataSource ds;
    @Mock
    private Connection conn;
    @Mock
    private Statement stmt;
    @Mock
    private ResultSet rs;

    @Test
    void getExistingUser() throws SQLException {
        final UserId id = UserId.of(1);
        final String username = "testuser";
        final String realname = "bosse";
        final String passwordHash = "xxx";
        final User expectedUser = new User(id, username, realname, passwordHash);
        when(ds.getConnection()).thenReturn(conn);
        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(contains(username))).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        when(rs.getInt("id")).thenReturn(id.getId());
        when(rs.getString("realname")).thenReturn(realname);
        when(rs.getString("password_hash")).thenReturn(passwordHash);
        UserDao userDao = new UserDao(ds);
        assertThat(userDao.getByName(username)).isEqualTo(Optional.of(expectedUser));
    }

    @Test
    void getNonExistingUser() throws SQLException {
        final String username = "testuser";
        when(ds.getConnection()).thenReturn(conn);
        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(contains(username))).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        UserDao userDao = new UserDao(ds);
        assertThat(userDao.getByName(username)).isEmpty();
    }
}