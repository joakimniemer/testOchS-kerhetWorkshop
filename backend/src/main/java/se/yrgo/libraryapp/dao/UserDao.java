package se.yrgo.libraryapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;

public class UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDao.class);
    private DataSource ds;

    @Inject
    UserDao(DataSource ds) {
        this.ds = ds;
    }

    public Optional<User> get(String user) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, realname, password_hash FROM user WHERE user = ?")) {
            ps.setString(1, user);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    UserId userId = UserId.of(id);
                    String realname = rs.getString("realname");
                    String passwordHash = rs.getString("password_hash");
                    return Optional.of(new User(userId, user, realname, passwordHash));
                }
            }
        } catch (SQLException ex) {
            logger.error("Unable to get user " + user, ex);
        }
        return Optional.empty();
    }

    public boolean register(String name, String realname, String passwordHash) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            return insertUserAndRole(name, realname, passwordHash, conn);
        } catch (SQLException ex) {
            logger.error("Unable to register user " + name, ex);
            return false;
        }
    }

    public boolean isNameAvailable(String name) {
        String query = "SELECT id FROM user WHERE user = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }
        } catch (SQLException ex) {
            logger.error("Unable to lookup user name " + name, ex);
            return false;
        }
    }

    private boolean insertUserAndRole(String name, String realname, String passwordHash, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO user (user, realname, password_hash) VALUES (?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, realname);
            ps.setString(3, passwordHash);

            UserId userId = null;
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                userId = getGeneratedUserId(ps);
            }


            if (userId.getId() > 0 && addToUserRole(conn, userId)) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException ex) {
            conn.rollback();
            logger.error("Unable to register user " + name, ex);
            return false;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    private UserId getGeneratedUserId(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            rs.next();
            return UserId.of(rs.getInt(1));
        }
    }

    private boolean addToUserRole(Connection conn, UserId user) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO user_role (user_id, role_id) VALUES (?, 2)")) {
            ps.setInt(1, user.getId());

            return ps.executeUpdate() == 1;
        }
    }
}

