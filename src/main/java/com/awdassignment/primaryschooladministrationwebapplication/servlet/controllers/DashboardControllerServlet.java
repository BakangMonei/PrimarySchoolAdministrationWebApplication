package com.awdassignment.primaryschooladministrationwebapplication.servlet.controllers;

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
import java.util.Map;

@WebServlet(name = "DashboardControllerServlet", urlPatterns = "/dashboard")
public class DashboardControllerServlet extends HttpServlet {

    private transient StudentDAO studentDAO;
    private transient TeacherDAO teacherDAO;
    private transient ClassDAO classDAO;

    @Override
    public void init() {
        this.studentDAO = new StudentDAOImpl();
        this.teacherDAO = new TeacherDAOImpl();
        this.classDAO = new ClassDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("stats", Map.of(
                "totalStudents", studentDAO.findAll().size(),
                "totalTeachers", teacherDAO.findAll().size(),
                "totalClasses", classDAO.findAll().size()
        ));
        req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(req, resp);
    }
}

