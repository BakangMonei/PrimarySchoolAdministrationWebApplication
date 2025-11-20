package com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Parent;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.ParentDAO;
import com.awdassignment.primaryschooladministrationwebapplication.util.DBConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
    import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ParentDAOImpl implements ParentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentDAOImpl.class);

    private static final String INSERT_SQL = """
            INSERT INTO parents (user_id, name, surname, contact_no, email, children_ids)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE parents SET
                user_id=?, name=?, surname=?, contact_no=?, email=?, children_ids=?
            WHERE id=?
            """;
    private static final String FIND_BY_ID_SQL = "SELECT * FROM parents WHERE id=?";
    private static final String FIND_BY_USER_SQL = "SELECT * FROM parents WHERE user_id=?";
    private static final String FIND_ALL_SQL = "SELECT * FROM parents ORDER BY surname, name";

    @Override
    public Long save(Parent parent) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, parent, false);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    parent.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save parent {}", parent.getEmail(), e);
            throw new RuntimeException("Failed to save parent", e);
        }
        return null;
    }

    @Override
    public void update(Parent parent) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            bind(ps, parent, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update parent {}", parent.getId(), e);
            throw new RuntimeException("Failed to update parent", e);
        }
    }

    @Override
    public Optional<Parent> findById(Long id) {
        return findSingle(FIND_BY_ID_SQL, ps -> ps.setLong(1, id));
    }

    @Override
    public Optional<Parent> findByUserId(Long userId) {
        return findSingle(FIND_BY_USER_SQL, ps -> ps.setLong(1, userId));
    }

    @Override
    public List<Parent> findAll() {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            List<Parent> parents = new ArrayList<>();
            while (rs.next()) {
                parents.add(map(rs));
            }
            return parents;
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch parents", e);
            throw new RuntimeException("Failed to fetch parents", e);
        }
    }

    private void bind(PreparedStatement ps, Parent parent, boolean includeIdAtEnd) throws SQLException {
        if (parent.getUserId() == null) {
            ps.setNull(1, Types.BIGINT);
        } else {
            ps.setLong(1, parent.getUserId());
        }
        ps.setString(2, parent.getName());
        ps.setString(3, parent.getSurname());
        ps.setString(4, parent.getContactNo());
        ps.setString(5, parent.getEmail());
        ps.setString(6, encodeChildren(parent.getChildrenIds()));
        if (includeIdAtEnd) {
            ps.setLong(7, parent.getId());
        }
    }

    private Optional<Parent> findSingle(String sql, SqlConsumer consumer) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Failed query {}", sql, e);
            throw new RuntimeException("Query failed", e);
        }
    }

    private Parent map(ResultSet rs) throws SQLException {
        Parent parent = new Parent();
        parent.setId(rs.getLong("id"));
        long userId = rs.getLong("user_id");
        parent.setUserId(rs.wasNull() ? null : userId);
        parent.setName(rs.getString("name"));
        parent.setSurname(rs.getString("surname"));
        parent.setContactNo(rs.getString("contact_no"));
        parent.setEmail(rs.getString("email"));
        parent.setChildrenIds(decodeChildren(rs.getString("children_ids")));
        return parent;
    }

    private String encodeChildren(List<Long> childrenIds) {
        if (childrenIds == null || childrenIds.isEmpty()) {
            return null;
        }
        return childrenIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    private List<Long> decodeChildren(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return List.of();
        }
        String[] tokens = encoded.split(",");
        List<Long> ids = new ArrayList<>();
        for (String token : tokens) {
            ids.add(Long.valueOf(token));
        }
        return ids;
    }

    @FunctionalInterface
    private interface SqlConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}

