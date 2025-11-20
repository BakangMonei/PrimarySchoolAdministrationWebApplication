package com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Student;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.StudentDAO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentDAOImpl implements StudentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAOImpl.class);

    private static final String INSERT_SQL = """
            INSERT INTO students
            (name, surname, birth_certificate_no, gender, date_of_birth, address, guardian_name,
             guardian_contact_no, guardian_email, registration_date, status, current_class,
             subjects, subject_grades, average_grade, notes, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_SQL = """
            UPDATE students SET
                name=?, surname=?, birth_certificate_no=?, gender=?, date_of_birth=?, address=?,
                guardian_name=?, guardian_contact_no=?, guardian_email=?,
                registration_date=?, status=?, current_class=?,
                subjects=?, subject_grades=?, average_grade=?, notes=?, updated_at=?
            WHERE id=?
            """;

    private static final String DELETE_SQL = "DELETE FROM students WHERE id=?";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM students WHERE id=?";
    private static final String FIND_BY_BC_SQL = "SELECT * FROM students WHERE birth_certificate_no=?";
    private static final String FIND_ALL_SQL = "SELECT * FROM students ORDER BY surname, name";

    private static final String SEARCH_SQL = """
            SELECT * FROM students
            WHERE (? IS NULL OR LOWER(name) LIKE ?)
              AND (? IS NULL OR LOWER(surname) LIKE ?)
              AND (? IS NULL OR LOWER(current_class) = LOWER(?))
              AND (? IS NULL OR LOWER(status) = LOWER(?))
              AND (? IS NULL OR date_of_birth = ?)
            ORDER BY surname, name
            """;

    private static final String TOP_STUDENTS_SQL = """
            SELECT * FROM students
            WHERE LOWER(current_class) = LOWER(?)
            ORDER BY average_grade DESC
            LIMIT ?
            """;

    @Override
    public Long save(Student student) {
        Objects.requireNonNull(student, "student");
        student.recalculateAverage();
        LocalDateTime now = LocalDateTime.now();
        student.setCreatedAt(now);
        student.setUpdatedAt(now);
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            bindStudentForInsert(ps, student);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    student.setId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to save student {}", student.getBirthCertificateNo(), e);
            throw new RuntimeException("Failed to save student", e);
        }
        return null;
    }

    @Override
    public void update(Student student) {
        Objects.requireNonNull(student, "student");
        student.recalculateAverage();
        student.setUpdatedAt(LocalDateTime.now());
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {
            bindStudentForUpdate(ps, student);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update student {}", student.getId(), e);
            throw new RuntimeException("Failed to update student", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete student {}", id, e);
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        return findSingle(FIND_BY_ID_SQL, ps -> ps.setLong(1, id));
    }

    @Override
    public Optional<Student> findByBirthCertificateNo(String birthCertificateNo) {
        return findSingle(FIND_BY_BC_SQL, ps -> ps.setString(1, birthCertificateNo));
    }

    @Override
    public List<Student> findAll() {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            return mapStudents(rs);
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch students", e);
            throw new RuntimeException("Failed to fetch students", e);
        }
    }

    @Override
    public List<Student> search(String name, String surname, String className, String status, LocalDate dob) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH_SQL)) {
            setNullable(ps, 1, name);
            ps.setString(2, like(name));
            setNullable(ps, 3, surname);
            ps.setString(4, like(surname));
            setNullable(ps, 5, className);
            ps.setString(6, className);
            setNullable(ps, 7, status);
            ps.setString(8, status);
            setNullable(ps, 9, dob);
            if (dob == null) {
                ps.setNull(10, Types.DATE);
            } else {
                ps.setDate(10, Date.valueOf(dob));
            }
            try (ResultSet rs = ps.executeQuery()) {
                return mapStudents(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to search students", e);
            throw new RuntimeException("Failed to search students", e);
        }
    }

    @Override
    public List<Student> findTopStudentsByClass(String className, int limit) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(TOP_STUDENTS_SQL)) {
            ps.setString(1, className);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                return mapStudents(rs);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch top students for class {}", className, e);
            throw new RuntimeException("Failed to fetch top students", e);
        }
    }

    private void bindCommonStudentFields(PreparedStatement ps, Student student) throws SQLException {
        ps.setString(1, student.getName());
        ps.setString(2, student.getSurname());
        ps.setString(3, student.getBirthCertificateNo());
        ps.setString(4, student.getGender());
        ps.setDate(5, Date.valueOf(student.getDateOfBirth()));
        ps.setString(6, student.getAddress());
        ps.setString(7, student.getGuardianName());
        ps.setString(8, student.getGuardianContactNo());
        ps.setString(9, student.getGuardianEmail());
        ps.setDate(10, Date.valueOf(student.getRegistrationDate()));
        ps.setString(11, student.getStatus());
        ps.setString(12, student.getCurrentClass());
        ps.setString(13, encodeSubjects(student.getSubjects()));
        ps.setString(14, encodeSubjectGrades(student.getSubjectGrades()));
        ps.setDouble(15, Optional.ofNullable(student.getAverageGrade()).orElse(0d));
        ps.setString(16, student.getNotes());
    }

    private void bindStudentForInsert(PreparedStatement ps, Student student) throws SQLException {
        bindCommonStudentFields(ps, student);
        ps.setTimestamp(17, Timestamp.valueOf(student.getCreatedAt()));
        ps.setTimestamp(18, Timestamp.valueOf(student.getUpdatedAt()));
    }

    private void bindStudentForUpdate(PreparedStatement ps, Student student) throws SQLException {
        bindCommonStudentFields(ps, student);
        ps.setTimestamp(17, Timestamp.valueOf(student.getUpdatedAt()));
        ps.setLong(18, student.getId());
    }

    private Optional<Student> findSingle(String sql, SqlConsumer consumer) {
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            consumer.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapStudent(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to execute query {}", sql, e);
            throw new RuntimeException("Failed query execution", e);
        }
    }

    private List<Student> mapStudents(ResultSet rs) throws SQLException {
        List<Student> students = new ArrayList<>();
        while (rs.next()) {
            students.add(mapStudent(rs));
        }
        return students;
    }

    private Student mapStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setName(rs.getString("name"));
        student.setSurname(rs.getString("surname"));
        student.setBirthCertificateNo(rs.getString("birth_certificate_no"));
        student.setGender(rs.getString("gender"));
        Date dob = rs.getDate("date_of_birth");
        student.setDateOfBirth(dob != null ? dob.toLocalDate() : null);
        student.setAddress(rs.getString("address"));
        student.setGuardianName(rs.getString("guardian_name"));
        student.setGuardianContactNo(rs.getString("guardian_contact_no"));
        student.setGuardianEmail(rs.getString("guardian_email"));
        Date registration = rs.getDate("registration_date");
        student.setRegistrationDate(registration != null ? registration.toLocalDate() : null);
        student.setStatus(rs.getString("status"));
        student.setCurrentClass(rs.getString("current_class"));
        student.setSubjects(decodeSubjects(rs.getString("subjects")));
        student.setSubjectGrades(decodeSubjectGrades(rs.getString("subject_grades")));
        student.setAverageGrade(rs.getDouble("average_grade"));
        student.setNotes(rs.getString("notes"));
        Timestamp created = rs.getTimestamp("created_at");
        student.setCreatedAt(created != null ? created.toLocalDateTime() : null);
        Timestamp updated = rs.getTimestamp("updated_at");
        student.setUpdatedAt(updated != null ? updated.toLocalDateTime() : null);
        return student;
    }

    private String encodeSubjects(List<String> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            return null;
        }
        return String.join(",", subjects);
    }

    private List<String> decodeSubjects(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(encoded.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private String encodeSubjectGrades(Map<String, Double> grades) {
        if (grades == null || grades.isEmpty()) {
            return null;
        }
        return grades.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("|"));
    }

    private Map<String, Double> decodeSubjectGrades(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return Collections.emptyMap();
        }
        Map<String, Double> grades = new LinkedHashMap<>();
        String[] tokens = encoded.split("\\|");
        for (String token : tokens) {
            String[] pair = token.split(":");
            if (pair.length == 2) {
                grades.put(pair[0], Double.valueOf(pair[1]));
            }
        }
        return grades;
    }

    private void setNullable(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.VARCHAR);
        } else if (value instanceof String string) {
            ps.setString(index, string);
        } else if (value instanceof LocalDate date) {
            ps.setDate(index, Date.valueOf(date));
        } else {
            throw new IllegalArgumentException("Unsupported type for nullable setter");
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

