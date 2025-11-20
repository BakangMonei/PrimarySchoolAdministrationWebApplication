package com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.User;
import com.awdassignment.primaryschooladministrationwebapplication.model.beans.UserRole;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.UserDAO;
import com.awdassignment.primaryschooladministrationwebapplication.util.DBConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    private static final String INSERT_SQL = """
            INSERT INTO users (username, hashed_password, role, email, active, created_at, last_login_at, remember_me_token)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE users SET
                username=?, hashed_password=?, role=?, email=?, active=?, last_login_at=?, remember_me_token=?
            WHERE id=?
            """;
    private static final String FIND_BY_ID_SQL = "SELECT * FROM users WHERE id=?";
    private static final String FIND_BY_USERNAME_SQL = "SELECT * FROM users WHERE username=?";
    private static final String FIND_BY_TOKEN_SQL = "SELECT * FROM users WHERE remember_me_token=?";
    private static final String FIND_BY_ROLE_SQL = "SELECT * FROM users WHERE role=? ORDER BY username";

    @Override
    public Long save(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, user, false);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    user.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save user {}", user.getUsername(), e);
            throw new RuntimeException("Failed to save user", e);
        }
        return null;
    }

    @Override
    public void update(User user) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            bind(ps, user, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update user {}", user.getId(), e);
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return findSingle(FIND_BY_ID_SQL, ps -> ps.setLong(1, id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findSingle(FIND_BY_USERNAME_SQL, ps -> ps.setString(1, username));
    }

    @Override
    public Optional<User> findByRememberMeToken(String token) {
        return findSingle(FIND_BY_TOKEN_SQL, ps -> ps.setString(1, token));
    }

    @Override
    public List<User> findByRole(UserRole role) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ROLE_SQL)) {
            ps.setString(1, role.name());
            try (ResultSet rs = ps.executeQuery()) {
                return mapUsers(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch users by role {}", role, e);
            throw new RuntimeException("Failed to fetch users by role", e);
        }
    }

    private void bind(PreparedStatement ps, User user, boolean includeIdAtEnd) throws SQLException {
        ps.setString(1, user.getUsername());
        ps.setString(2, user.getHashedPassword());
        ps.setString(3, user.getRole().name());
        ps.setString(4, user.getEmail());
        ps.setBoolean(5, user.isActive());
        Timestamp created = user.getCreatedAt() != null ? Timestamp.valueOf(user.getCreatedAt()) : Timestamp.valueOf(LocalDateTime.now());
        ps.setTimestamp(6, created);
        if (user.getLastLoginAt() == null) {
            ps.setNull(7, Types.TIMESTAMP);
        } else {
            ps.setTimestamp(7, Timestamp.valueOf(user.getLastLoginAt()));
        }
        ps.setString(8, user.getRememberMeToken());
        if (includeIdAtEnd) {
            ps.setLong(9, user.getId());
        }
    }

    private Optional<User> findSingle(String sql, SqlConsumer consumer) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapUser(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Query failed {}", sql, e);
            throw new RuntimeException("Query failed", e);
        }
    }

    private List<User> mapUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            users.add(mapUser(rs));
        }
        return users;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setHashedPassword(rs.getString("hashed_password"));
        user.setRole(UserRole.valueOf(rs.getString("role")));
        user.setEmail(rs.getString("email"));
        user.setActive(rs.getBoolean("active"));
        Timestamp created = rs.getTimestamp("created_at");
        user.setCreatedAt(created != null ? created.toLocalDateTime() : null);
        Timestamp lastLogin = rs.getTimestamp("last_login_at");
        user.setLastLoginAt(lastLogin != null ? lastLogin.toLocalDateTime() : null);
        user.setRememberMeToken(rs.getString("remember_me_token"));
        return user;
    }

    @FunctionalInterface
    private interface SqlConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}

