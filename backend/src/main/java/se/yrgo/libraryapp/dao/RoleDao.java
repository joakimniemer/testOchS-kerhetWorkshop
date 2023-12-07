package se.yrgo.libraryapp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;

public class RoleDao {
    private static Logger logger = LoggerFactory.getLogger(RoleDao.class);
    private DataSource ds;

    @Inject
    RoleDao(DataSource ds) {
        this.ds = ds;
    }

    public List<Role> get(UserId userId) throws SQLException {
        List<Role> roles = new ArrayList<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT r.role FROM user_role AS ur JOIN role AS r ON ur.role_id = r.id WHERE ur.user_id = ?")) {

            ps.setInt(1, userId.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    roles.add(Role.fromString(rs.getString("r.role")));
                }
            }
            return roles;
            //Här skulle man kunna skicka in input som gör att man får ut mer än det är tänk. Men jag tror dock bara man kan få ut
            //de olika rollerna (admin, user) utan någon tillhörande data, så känns kanska harmlöst här?
            //Inser nu också att inputen har tagits emot som ett "UserId" vilket också gör det säkrare.
            //Det är heller ingen string som tas in så då blir det mer säkert?
        } catch (
                SQLException ex) {
            logger.error("Unable to get user id", ex);
            return List.of();
        }
    }
}
