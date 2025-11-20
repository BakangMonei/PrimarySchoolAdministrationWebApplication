package com.awdassignment.primaryschooladministrationwebapplication.servlet.controllers;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Student;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.StudentDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.StudentDAOImpl;
import com.awdassignment.primaryschooladministrationwebapplication.util.Constants;
import com.awdassignment.primaryschooladministrationwebapplication.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "StudentControllerServlet", urlPatterns = {"/students/*"})
public class StudentControllerServlet extends HttpServlet {

    private transient StudentDAO studentDAO;

    @Override
    public void init() {
        this.studentDAO = new StudentDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");
        switch (path) {
            case "/new" -> showForm(req, resp, new Student(), "/students/create");
            case "/edit" -> showEditForm(req, resp);
            case "/view" -> showDetails(req, resp);
            case "/top" -> showTopStudents(req, resp);
            default -> listStudents(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");
        switch (path) {
            case "/create" -> createStudent(req, resp);
            case "/update" -> updateStudent(req, resp);
            case "/delete" -> deleteStudent(req, resp);
            case "/assign-grade" -> assignGrade(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/students");
        }
    }

    private void listStudents(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String className = req.getParameter("class");
        String status = req.getParameter("status");
        LocalDate dob = Optional.ofNullable(req.getParameter("dob")).filter(s -> !s.isBlank()).map(LocalDate::parse).orElse(null);
        List<Student> students = studentDAO.search(name, surname, className, status, dob);
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("surname", surname);
        filters.put("class", className);
        filters.put("status", status);
        filters.put("dob", dob != null ? dob.toString() : "");
        req.setAttribute("students", students);
        req.setAttribute("filters", filters);
        req.getRequestDispatcher("/WEB-INF/views/student/studentList.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Student student, String action) throws ServletException, IOException {
        req.setAttribute("student", student);
        req.setAttribute("formAction", action);
        req.getRequestDispatcher("/WEB-INF/views/student/studentForm.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Student student = studentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        showForm(req, resp, student, "/students/update");
    }

    private void showDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Student student = studentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        req.setAttribute(Constants.ATTR_SELECTED_STUDENT, student);
        req.getRequestDispatcher("/WEB-INF/views/student/studentDetails.jsp").forward(req, resp);
    }

    private void showTopStudents(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String className = req.getParameter("class");
        List<Student> topStudents = studentDAO.findTopStudentsByClass(className, 5);
        req.setAttribute("topStudents", topStudents);
        req.setAttribute("selectedClass", className);
        req.getRequestDispatcher("/WEB-INF/views/student/topStudents.jsp").forward(req, resp);
    }

    private void createStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Student student = readStudentFromRequest(req);
        ValidationUtils.requireNonBlank(student.getBirthCertificateNo(), "Birth certificate number is required");
        studentDAO.save(student);
        resp.sendRedirect(req.getContextPath() + "/students?success=created");
    }

    private void updateStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Student student = readStudentFromRequest(req);
        student.setId(Long.valueOf(req.getParameter("id")));
        studentDAO.update(student);
        resp.sendRedirect(req.getContextPath() + "/students/view?id=" + student.getId());
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        studentDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/students?success=deleted");
    }

    private void assignGrade(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        String subject = req.getParameter("subject");
        Double grade = Double.valueOf(req.getParameter("grade"));
        Student student = studentDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        student.upsertGrade(subject, grade);
        studentDAO.update(student);
        resp.sendRedirect(req.getContextPath() + "/students/view?id=" + id + "&success=grade");
    }

    private Student readStudentFromRequest(HttpServletRequest req) {
        Student student = new Student();
        student.setName(req.getParameter("name"));
        student.setSurname(req.getParameter("surname"));
        student.setBirthCertificateNo(req.getParameter("birthCertificateNo"));
        student.setGender(req.getParameter("gender"));
        student.setDateOfBirth(LocalDate.parse(req.getParameter("dateOfBirth")));
        student.setAddress(req.getParameter("address"));
        student.setGuardianName(req.getParameter("guardianName"));
        student.setGuardianContactNo(req.getParameter("guardianContactNo"));
        student.setGuardianEmail(req.getParameter("guardianEmail"));
        student.setRegistrationDate(LocalDate.parse(req.getParameter("registrationDate")));
        student.setStatus(req.getParameter("status"));
        student.setCurrentClass(req.getParameter("currentClass"));
        student.setSubjects(parseList(req.getParameter("subjects")));
        student.setSubjectGrades(parseGrades(req.getParameter("subjectGrades")));
        student.setNotes(req.getParameter("notes"));
        return student;
    }

    private List<String> parseList(String csv) {
        if (csv == null || csv.isBlank()) {
            return List.of();
        }
        return List.of(csv.split(",")).stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }

    private java.util.Map<String, Double> parseGrades(String gradesParam) {
        if (gradesParam == null || gradesParam.isBlank()) {
            return Map.of();
        }
        return List.of(gradesParam.split(",")).stream()
                .map(pair -> pair.split(":"))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                        parts -> parts[0].trim(),
                        parts -> Double.valueOf(parts[1].trim())
                ));
    }
}

