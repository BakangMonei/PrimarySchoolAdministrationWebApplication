package com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Teacher;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.TeacherDAO;
import com.awdassignment.primaryschooladministrationwebapplication.util.DBConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeacherDAOImpl implements TeacherDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAOImpl.class);

    private static final String INSERT_SQL = """
            INSERT INTO teachers
            (name, surname, omang_or_passport_no, gender, date_of_birth, address, contact_no,
             email, qualifications, subjects_qualified, class_subject_map, date_joined,
             created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE teachers SET
                name=?, surname=?, omang_or_passport_no=?, gender=?, date_of_birth=?, address=?, contact_no=?,
                email=?, qualifications=?, subjects_qualified=?, class_subject_map=?,
                date_joined=?, updated_at=?
            WHERE id=?
            """;

    private static final String DELETE_SQL = "DELETE FROM teachers WHERE id=?";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM teachers WHERE id=?";
    private static final String FIND_BY_OMANG_SQL = "SELECT * FROM teachers WHERE omang_or_passport_no=?";
    private static final String FIND_ALL_SQL = "SELECT * FROM teachers ORDER BY surname, name";
    private static final String SEARCH_SQL = """
            SELECT * FROM teachers
            WHERE (? IS NULL OR LOWER(name) LIKE ? OR LOWER(surname) LIKE ?)
              AND (? IS NULL OR LOWER(subjects_qualified) LIKE ?)
            ORDER BY surname, name
            """;

    @Override
    public Long save(Teacher teacher) {
        LocalDateTime now = LocalDateTime.now();
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            bindTeacherForInsert(ps, teacher);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    teacher.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save teacher {}", teacher.getOmangOrPassportNo(), e);
            throw new RuntimeException("Failed to save teacher", e);
        }
        return null;
    }

    @Override
    public void update(Teacher teacher) {
        teacher.setUpdatedAt(LocalDateTime.now());
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            bindTeacherForUpdate(ps, teacher);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update teacher {}", teacher.getId(), e);
            throw new RuntimeException("Failed to update teacher", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete teacher {}", id, e);
            throw new RuntimeException("Failed to delete teacher", e);
        }
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return findSingle(FIND_BY_ID_SQL, ps -> ps.setLong(1, id));
    }

    @Override
    public Optional<Teacher> findByOmang(String omangOrPassportNo) {
        return findSingle(FIND_BY_OMANG_SQL, ps -> ps.setString(1, omangOrPassportNo));
    }

    @Override
    public List<Teacher> findAll() {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            return mapTeachers(rs);
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch teachers", e);
            throw new RuntimeException("Failed to fetch teachers", e);
        }
    }

    @Override
    public List<Teacher> search(String name, String subject) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_SQL)) {
            setNullable(ps, 1, name);
            ps.setString(2, like(name));
            ps.setString(3, like(name));
            setNullable(ps, 4, subject);
            ps.setString(5, like(subject));
            try (ResultSet rs = ps.executeQuery()) {
                return mapTeachers(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to search teachers", e);
            throw new RuntimeException("Failed to search teachers", e);
        }
    }

    private void bindCommonTeacherFields(PreparedStatement ps, Teacher teacher) throws SQLException {
        ps.setString(1, teacher.getName());
        ps.setString(2, teacher.getSurname());
        ps.setString(3, teacher.getOmangOrPassportNo());
        ps.setString(4, teacher.getGender());
        ps.setDate(5, teacher.getDateOfBirth() != null ? Date.valueOf(teacher.getDateOfBirth()) : null);
        ps.setString(6, teacher.getAddress());
        ps.setString(7, teacher.getContactNo());
        ps.setString(8, teacher.getEmail());
        ps.setString(9, teacher.getQualifications());
        ps.setString(10, encodeList(teacher.getSubjectsQualifiedToTeach()));
        ps.setString(11, encodeMap(teacher.getClassToSubjectMap()));
        ps.setDate(12, teacher.getDateJoined() != null ? Date.valueOf(teacher.getDateJoined()) : null);
    }

    private void bindTeacherForInsert(PreparedStatement ps, Teacher teacher) throws SQLException {
        bindCommonTeacherFields(ps, teacher);
        ps.setTimestamp(13, Timestamp.valueOf(teacher.getCreatedAt()));
        ps.setTimestamp(14, Timestamp.valueOf(teacher.getUpdatedAt()));
    }

    private void bindTeacherForUpdate(PreparedStatement ps, Teacher teacher) throws SQLException {
        bindCommonTeacherFields(ps, teacher);
        ps.setTimestamp(13, Timestamp.valueOf(teacher.getUpdatedAt()));
        ps.setLong(14, teacher.getId());
    }

    private Optional<Teacher> findSingle(String sql, SqlConsumer consumer) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapTeacher(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Failed query {}", sql, e);
            throw new RuntimeException("Query failed", e);
        }
    }

    private List<Teacher> mapTeachers(ResultSet rs) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        while (rs.next()) {
            teachers.add(mapTeacher(rs));
        }
        return teachers;
    }

    private Teacher mapTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getLong("id"));
        teacher.setName(rs.getString("name"));
        teacher.setSurname(rs.getString("surname"));
        teacher.setOmangOrPassportNo(rs.getString("omang_or_passport_no"));
        teacher.setGender(rs.getString("gender"));
        Date dob = rs.getDate("date_of_birth");
        teacher.setDateOfBirth(dob != null ? dob.toLocalDate() : null);
        teacher.setAddress(rs.getString("address"));
        teacher.setContactNo(rs.getString("contact_no"));
        teacher.setEmail(rs.getString("email"));
        teacher.setQualifications(rs.getString("qualifications"));
        teacher.setSubjectsQualifiedToTeach(decodeList(rs.getString("subjects_qualified")));
        teacher.setClassToSubjectMap(decodeMap(rs.getString("class_subject_map")));
        Date joined = rs.getDate("date_joined");
        teacher.setDateJoined(joined != null ? joined.toLocalDate() : null);
        Timestamp created = rs.getTimestamp("created_at");
        teacher.setCreatedAt(created != null ? created.toLocalDateTime() : null);
        Timestamp updated = rs.getTimestamp("updated_at");
        teacher.setUpdatedAt(updated != null ? updated.toLocalDateTime() : null);
        return teacher;
    }

    private String encodeList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return String.join(",", values);
    }

    private List<String> decodeList(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(encoded.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private String encodeMap(Map<String, String> mapping) {
        if (mapping == null || mapping.isEmpty()) {
            return null;
        }
        return mapping.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private Map<String, String> decodeMap(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new LinkedHashMap<>();
        for (String pair : encoded.split("\\|")) {
            String[] tokens = pair.split(":");
            if (tokens.length == 2) {
                map.put(tokens[0], tokens[1]);
            }
        }
        return map;
    }

    private void setNullable(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value);
        }
    }

    private String like(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return "%" + value.toLowerCase() + "%";
    }

    @FunctionalInterface
    private interface SqlConsumer {
        void accept(PreparedStatement ps) throws SQLException;
    }
}

