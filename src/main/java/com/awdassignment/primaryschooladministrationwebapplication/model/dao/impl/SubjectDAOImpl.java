package com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Subject;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.SubjectDAO;
import com.awdassignment.primaryschooladministrationwebapplication.util.DBConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectDAOImpl implements SubjectDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectDAOImpl.class);

    private static final String INSERT_SQL = "INSERT INTO subjects (name, description) VALUES (?, ?)";
    private static final String UPDATE_SQL = "UPDATE subjects SET name=?, description=? WHERE id=?";
    private static final String DELETE_SQL = "DELETE FROM subjects WHERE id=?";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM subjects WHERE id=?";
    private static final String FIND_BY_NAME_SQL = "SELECT * FROM subjects WHERE LOWER(name)=LOWER(?)";
    private static final String FIND_ALL_SQL = "SELECT * FROM subjects ORDER BY name";

    @Override
    public Long save(Subject subject) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDescription());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    subject.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save subject {}", subject.getName(), e);
            throw new RuntimeException("Failed to save subject", e);
        }
        return null;
    }

    @Override
    public void update(Subject subject) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDescription());
            ps.setLong(3, subject.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update subject {}", subject.getId(), e);
            throw new RuntimeException("Failed to update subject", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete subject {}", id, e);
            throw new RuntimeException("Failed to delete subject", e);
        }
    }

    @Override
    public Optional<Subject> findById(Long id) {
        return findSingle(FIND_BY_ID_SQL, ps -> ps.setLong(1, id));
    }

    @Override
    public Optional<Subject> findByName(String name) {
        return findSingle(FIND_BY_NAME_SQL, ps -> ps.setString(1, name));
    }

    @Override
    public List<Subject> findAll() {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            List<Subject> subjects = new ArrayList<>();
            while (rs.next()) {
                subjects.add(map(rs));
            }
            return subjects;
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch subjects", e);
            throw new RuntimeException("Failed to fetch subjects", e);
        }
    }

    private Subject map(ResultSet rs) throws SQLException {
        Subject subject = new Subject();
        subject.setId(rs.getLong("id"));
        subject.setName(rs.getString("name"));
        subject.setDescription(rs.getString("description"));
        return subject;
    }

    private Optional<Subject> findSingle(String sql, SqlConsumer consumer) {
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

    @FunctionalInterface
    private interface SqlConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}

