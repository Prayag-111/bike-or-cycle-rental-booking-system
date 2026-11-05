package com.bikerental.servlet;

import com.bikerental.exception.InvalidUserDataException;
import com.bikerental.model.User;
import com.bikerental.service.UserOperations;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RegisterServlet.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * GET  /register : sends the visitor to the registration page.
 * POST /register : validates the submitted data on the server and registers the user.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final AtomicInteger NEXT_ID = new AtomicInteger(100);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.sendRedirect("register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = clean(request.getParameter("fullName"));
        String username = clean(request.getParameter("username"));
        String email = clean(request.getParameter("email"));
        String phoneNumber = clean(request.getParameter("phoneNumber"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        UserOperations userOps =
                (UserOperations) getServletContext().getAttribute(AppListener.USER_OPS);

        try {
            if (password == null || !password.equals(confirmPassword)) {
                throw new InvalidUserDataException("Passwords do not match.");
            }

            User user = new User("U" + NEXT_ID.incrementAndGet(), fullName, username,
                    password, email, phoneNumber);
            userOps.registerUser(user);

            // Redirect after a successful POST so a page refresh does not register twice
            response.sendRedirect("login.jsp?registered=1");

        } catch (InvalidUserDataException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }
}
