package com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.SchoolClass;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.ClassDAO;
import com.awdassignment.primaryschooladministrationwebapplication.util.DBConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDAOImpl implements ClassDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassDAOImpl.class);

    private static final String INSERT_SQL = """
            INSERT INTO classes (name, description, class_teacher_id, capacity, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE classes SET
                name=?, description=?, class_teacher_id=?, capacity=?, updated_at=?
            WHERE id=?
            """;
    private static final String DELETE_SQL = "DELETE FROM classes WHERE id=?";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM classes WHERE id=?";
    private static final String FIND_BY_NAME_SQL = "SELECT * FROM classes WHERE LOWER(name)=LOWER(?)";
    private static final String FIND_ALL_SQL = "SELECT * FROM classes ORDER BY name";

    @Override
    public Long save(SchoolClass schoolClass) {
        LocalDateTime now = LocalDateTime.now();
        schoolClass.setCreatedAt(now);
        schoolClass.setUpdatedAt(now);
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            bindForInsert(ps, schoolClass);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    schoolClass.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save class {}", schoolClass.getName(), e);
            throw new RuntimeException("Failed to save class", e);
        }
        return null;
    }

    @Override
    public void update(SchoolClass schoolClass) {
        schoolClass.setUpdatedAt(LocalDateTime.now());
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            bindForUpdate(ps, schoolClass);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update class {}", schoolClass.getId(), e);
            throw new RuntimeException("Failed to update class", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete class {}", id, e);
            throw new RuntimeException("Failed to delete class", e);
        }
    }

    @Override
    public Optional<SchoolClass> findById(Long id) {
        return findSingle(FIND_BY_ID_SQL, ps -> ps.setLong(1, id));
    }

    @Override
    public Optional<SchoolClass> findByName(String name) {
        return findSingle(FIND_BY_NAME_SQL, ps -> ps.setString(1, name));
    }

    @Override
    public List<SchoolClass> findAll() {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            List<SchoolClass> classes = new ArrayList<>();
            while (rs.next()) {
                classes.add(map(rs));
            }
            return classes;
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch classes", e);
            throw new RuntimeException("Failed to fetch classes", e);
        }
    }

    private void bindCommon(PreparedStatement ps, SchoolClass schoolClass) throws SQLException {
        ps.setString(1, schoolClass.getName());
        ps.setString(2, schoolClass.getDescription());
        if (schoolClass.getClassTeacherId() == null) {
            ps.setNull(3, Types.BIGINT);
        } else {
            ps.setLong(3, schoolClass.getClassTeacherId());
        }
        ps.setInt(4, schoolClass.getCapacity());
    }

    private void bindForInsert(PreparedStatement ps, SchoolClass schoolClass) throws SQLException {
        bindCommon(ps, schoolClass);
        ps.setTimestamp(5, Timestamp.valueOf(schoolClass.getCreatedAt()));
        ps.setTimestamp(6, Timestamp.valueOf(schoolClass.getUpdatedAt()));
    }

    private void bindForUpdate(PreparedStatement ps, SchoolClass schoolClass) throws SQLException {
        bindCommon(ps, schoolClass);
        ps.setTimestamp(5, Timestamp.valueOf(schoolClass.getUpdatedAt()));
        ps.setLong(6, schoolClass.getId());
    }

    private Optional<SchoolClass> findSingle(String sql, SqlConsumer consumer) {
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

    private SchoolClass map(ResultSet rs) throws SQLException {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setId(rs.getLong("id"));
        schoolClass.setName(rs.getString("name"));
        schoolClass.setDescription(rs.getString("description"));
        long classTeacherId = rs.getLong("class_teacher_id");
        schoolClass.setClassTeacherId(rs.wasNull() ? null : classTeacherId);
        schoolClass.setCapacity(rs.getInt("capacity"));
        Timestamp created = rs.getTimestamp("created_at");
        schoolClass.setCreatedAt(created != null ? created.toLocalDateTime() : null);
        Timestamp updated = rs.getTimestamp("updated_at");
        schoolClass.setUpdatedAt(updated != null ? updated.toLocalDateTime() : null);
        return schoolClass;
    }

    @FunctionalInterface
    private interface SqlConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}

