package com.awdassignment.primaryschooladministrationwebapplication.servlet.controllers;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Teacher;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.TeacherDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.TeacherDAOImpl;
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

@WebServlet(name = "TeacherControllerServlet", urlPatterns = "/teachers/*")
public class TeacherControllerServlet extends HttpServlet {

    private transient TeacherDAO teacherDAO;

    @Override
    public void init() {
        this.teacherDAO = new TeacherDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");
        switch (path) {
            case "/new" -> showForm(req, resp, new Teacher(), "/teachers/create");
            case "/edit" -> showEditForm(req, resp);
            case "/view" -> showDetails(req, resp);
            default -> listTeachers(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");
        switch (path) {
            case "/create" -> createTeacher(req, resp);
            case "/update" -> updateTeacher(req, resp);
            case "/delete" -> deleteTeacher(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/teachers");
        }
    }

    private void listTeachers(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String subject = req.getParameter("subject");
        List<Teacher> teachers = teacherDAO.search(name, subject);
        Map<String, Object> filters = new HashMap<>();
        filters.put("name", name);
        filters.put("subject", subject);
        req.setAttribute("teachers", teachers);
        req.setAttribute("filters", filters);
        req.getRequestDispatcher("/WEB-INF/views/teacher/teacherList.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Teacher teacher, String action) throws ServletException, IOException {
        req.setAttribute("teacher", teacher);
        req.setAttribute("formAction", action);
        req.getRequestDispatcher("/WEB-INF/views/teacher/teacherForm.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Teacher teacher = teacherDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        showForm(req, resp, teacher, "/teachers/update");
    }

    private void showDetails(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        Teacher teacher = teacherDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Teacher not found"));
        req.setAttribute("teacher", teacher);
        req.getRequestDispatcher("/WEB-INF/views/teacher/teacherDetails.jsp").forward(req, resp);
    }

    private void createTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Teacher teacher = readTeacherFromRequest(req);
        teacherDAO.save(teacher);
        resp.sendRedirect(req.getContextPath() + "/teachers?success=created");
    }

    private void updateTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Teacher teacher = readTeacherFromRequest(req);
        teacher.setId(Long.valueOf(req.getParameter("id")));
        teacherDAO.update(teacher);
        resp.sendRedirect(req.getContextPath() + "/teachers/view?id=" + teacher.getId());
    }

    private void deleteTeacher(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        teacherDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/teachers?success=deleted");
    }

    private Teacher readTeacherFromRequest(HttpServletRequest req) {
        Teacher teacher = new Teacher();
        teacher.setName(req.getParameter("name"));
        teacher.setSurname(req.getParameter("surname"));
        teacher.setOmangOrPassportNo(req.getParameter("omang"));
        teacher.setGender(req.getParameter("gender"));
        Optional.ofNullable(req.getParameter("dateOfBirth")).filter(s -> !s.isBlank())
                .map(LocalDate::parse).ifPresent(teacher::setDateOfBirth);
        teacher.setAddress(req.getParameter("address"));
        teacher.setContactNo(req.getParameter("contactNo"));
        teacher.setEmail(req.getParameter("email"));
        teacher.setQualifications(req.getParameter("qualifications"));
        teacher.setSubjectsQualifiedToTeach(parseList(req.getParameter("subjectsQualified")));
        teacher.setClassToSubjectMap(parseMap(req.getParameter("classSubjectMap")));
        Optional.ofNullable(req.getParameter("dateJoined")).filter(s -> !s.isBlank())
                .map(LocalDate::parse).ifPresent(teacher::setDateJoined);
        return teacher;
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

    private Map<String, String> parseMap(String entries) {
        if (entries == null || entries.isBlank()) {
            return Map.of();
        }
        return List.of(entries.split(",")).stream()
                .map(pair -> pair.split(":"))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.toMap(
                        parts -> parts[0].trim(),
                        parts -> parts[1].trim()
                ));
    }
}

