package com.awdassignment.primaryschooladministrationwebapplication.servlet.controllers;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.SchoolClass;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.ClassDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.StudentDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.TeacherDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.ClassDAOImpl;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.StudentDAOImpl;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.TeacherDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ClassControllerServlet", urlPatterns = "/classes/*")
public class ClassControllerServlet extends HttpServlet {

    private transient ClassDAO classDAO;
    private transient StudentDAO studentDAO;
    private transient TeacherDAO teacherDAO;

    @Override
    public void init() {
        this.classDAO = new ClassDAOImpl();
        this.studentDAO = new StudentDAOImpl();
        this.teacherDAO = new TeacherDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");
        switch (path) {
            case "/new" -> showForm(req, resp, new SchoolClass(), "/classes/create");
            case "/edit" -> showEditForm(req, resp);
            default -> listClasses(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/");
        switch (path) {
            case "/create" -> createClass(req, resp);
            case "/update" -> updateClass(req, resp);
            case "/delete" -> deleteClass(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/classes");
        }
    }

    private void listClasses(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SchoolClass> classes = classDAO.findAll();
        req.setAttribute("classes", classes);
        req.getRequestDispatcher("/WEB-INF/views/class/classList.jsp").forward(req, resp);
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, SchoolClass schoolClass, String action) throws ServletException, IOException {
        req.setAttribute("schoolClass", schoolClass);
        req.setAttribute("formAction", action);
        req.setAttribute("teachers", teacherDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/class/classForm.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        SchoolClass schoolClass = classDAO.findById(id).orElseThrow(() -> new IllegalArgumentException("Class not found"));
        showForm(req, resp, schoolClass, "/classes/update");
    }

    private void createClass(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SchoolClass schoolClass = readClass(req);
        classDAO.save(schoolClass);
        resp.sendRedirect(req.getContextPath() + "/classes?success=created");
    }

    private void updateClass(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SchoolClass schoolClass = readClass(req);
        schoolClass.setId(Long.valueOf(req.getParameter("id")));
        classDAO.update(schoolClass);
        resp.sendRedirect(req.getContextPath() + "/classes?success=updated");
    }

    private void deleteClass(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        long studentCount = studentDAO.findAll().stream()
                .filter(student -> String.valueOf(student.getCurrentClass()).equalsIgnoreCase(req.getParameter("name")))
                .count();
        if (studentCount > 0) {
            resp.sendRedirect(req.getContextPath() + "/classes?error=class_has_students");
            return;
        }
        classDAO.delete(id);
        resp.sendRedirect(req.getContextPath() + "/classes?success=deleted");
    }

    private SchoolClass readClass(HttpServletRequest req) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(req.getParameter("name"));
        schoolClass.setDescription(req.getParameter("description"));
        String teacherIdParam = req.getParameter("classTeacherId");
        if (teacherIdParam != null && !teacherIdParam.isBlank()) {
            schoolClass.setClassTeacherId(Long.valueOf(teacherIdParam));
        }
        schoolClass.setCapacity(Integer.parseInt(Optional.ofNullable(req.getParameter("capacity")).orElse("35")));
        return schoolClass;
    }
}

