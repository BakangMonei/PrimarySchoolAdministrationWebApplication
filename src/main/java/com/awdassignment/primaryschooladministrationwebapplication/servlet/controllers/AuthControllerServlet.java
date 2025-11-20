package com.awdassignment.primaryschooladministrationwebapplication.servlet.controllers;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.Parent;
import com.awdassignment.primaryschooladministrationwebapplication.model.beans.User;
import com.awdassignment.primaryschooladministrationwebapplication.model.beans.UserRole;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.ParentDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.UserDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.ParentDAOImpl;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.UserDAOImpl;
import com.awdassignment.primaryschooladministrationwebapplication.util.Constants;
import com.awdassignment.primaryschooladministrationwebapplication.util.CookieUtils;
import com.awdassignment.primaryschooladministrationwebapplication.util.PasswordUtils;
import com.awdassignment.primaryschooladministrationwebapplication.util.ValidationUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "AuthControllerServlet", urlPatterns = "/auth/*")
public class AuthControllerServlet extends HttpServlet {

    private transient UserDAO userDAO;
    private transient ParentDAO parentDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAOImpl();
        this.parentDAO = new ParentDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/login");
        switch (path) {
            case "/register" -> req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            case "/logout" -> logout(req, resp);
            default -> req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = Optional.ofNullable(req.getPathInfo()).orElse("/login");
        switch (path) {
            case "/login" -> login(req, resp);
            case "/register" -> register(req, resp);
            default -> resp.sendRedirect(req.getContextPath() + "/auth/login");
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        boolean rememberMe = "on".equals(req.getParameter("rememberMe"));

        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent() && PasswordUtils.verifyPassword(password, userOpt.get().getHashedPassword()) && userOpt.get().isActive()) {
            User user = userOpt.get();
            HttpSession session = req.getSession(true);
            session.setAttribute(Constants.ATTR_LOGGED_IN_USER, user);

            if (rememberMe) {
                String token = UUID.randomUUID().toString();
                user.setRememberMeToken(token);
                CookieUtils.addSecureCookie(resp, Constants.COOKIE_REMEMBER_ME, token, 7 * 24 * 60 * 60);
            } else {
                user.setRememberMeToken(null);
                CookieUtils.deleteCookie(resp, Constants.COOKIE_REMEMBER_ME);
            }
            user.setLastLoginAt(LocalDateTime.now());
            userDAO.update(user);

            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            req.setAttribute("error", "Invalid credentials");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        CookieUtils.deleteCookie(resp, Constants.COOKIE_REMEMBER_ME);
        resp.sendRedirect(req.getContextPath() + "/auth/login?logout=true");
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String contactNo = req.getParameter("contactNo");

        if (!ValidationUtils.isValidEmail(email) || !password.equals(confirmPassword)) {
            req.setAttribute("error", "Validation failed. Check email format and passwords.");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (userDAO.findByUsername(username).isPresent()) {
            req.setAttribute("error", "Username already exists");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(UserRole.PARENT);
        user.setHashedPassword(PasswordUtils.hashPassword(password));
        userDAO.save(user);

        Parent parent = new Parent();
        parent.setUserId(user.getId());
        parent.setName(name);
        parent.setSurname(surname);
        parent.setContactNo(contactNo);
        parent.setEmail(email);
        parentDAO.save(parent);

        resp.sendRedirect(req.getContextPath() + "/auth/login?registered=true");
    }
}

