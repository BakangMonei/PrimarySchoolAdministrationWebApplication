package com.awdassignment.primaryschooladministrationwebapplication.filter;

import com.awdassignment.primaryschooladministrationwebapplication.model.beans.User;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.UserDAO;
import com.awdassignment.primaryschooladministrationwebapplication.model.dao.impl.UserDAOImpl;
import com.awdassignment.primaryschooladministrationwebapplication.util.Constants;
import com.awdassignment.primaryschooladministrationwebapplication.util.CookieUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter(filterName = "AuthFilter", urlPatterns = {
        "/dashboard",
        "/dashboard/*",
        "/students",
        "/students/*",
        "/teachers",
        "/teachers/*",
        "/classes",
        "/classes/*"
})
public class AuthFilter implements Filter {

    private transient UserDAO userDAO;

    public void init(FilterConfig filterConfig) {
        this.userDAO = new UserDAOImpl();
    }

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/auth/login",
            "/auth/logout",
            "/auth/register"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestUri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestUri.substring(contextPath.length());

        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(path::startsWith);
        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        User loggedInUser = (User) httpRequest.getSession().getAttribute(Constants.ATTR_LOGGED_IN_USER);
        if (loggedInUser == null) {
            CookieUtils.findCookie(httpRequest.getCookies(), Constants.COOKIE_REMEMBER_ME)
                    .flatMap(cookie -> userDAO.findByRememberMeToken(cookie.getValue()))
                    .ifPresent(user -> httpRequest.getSession(true).setAttribute(Constants.ATTR_LOGGED_IN_USER, user));
            loggedInUser = (User) httpRequest.getSession().getAttribute(Constants.ATTR_LOGGED_IN_USER);
        }
        if (loggedInUser == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/auth/login");
            return;
        }

        chain.doFilter(request, response);
    }
}

