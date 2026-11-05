package com.bikerental.servlet;

import com.bikerental.exception.AuthenticationException;
import com.bikerental.model.User;
import com.bikerental.service.UserOperations;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginServlet.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * GET  /login : opens the dashboard if already logged in, otherwise the login page.
 * POST /login : checks the credentials on the server and starts an HttpSession.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    public static final String SESSION_USER = "user";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_USER) != null) {
            response.sendRedirect("dashboard.jsp");
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserOperations userOps =
                (UserOperations) getServletContext().getAttribute(AppListener.USER_OPS);

        try {
            if (username == null || username.trim().isEmpty()
                    || password == null || password.isEmpty()) {
                throw new AuthenticationException("Enter both username and password.");
            }

            User user = userOps.authenticateUser(username.trim(), password);

            // Always start a fresh session after login (prevents session fixation)
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_USER, user);
            session.setMaxInactiveInterval(30 * 60);   // 30 minutes of inactivity

            response.sendRedirect("dashboard.jsp");

        } catch (AuthenticationException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
