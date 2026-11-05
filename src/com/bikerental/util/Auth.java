package com.bikerental.util;

import com.bikerental.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Auth.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Small helper that returns the logged-in user of a request, or null if nobody is logged in.
 */
public final class Auth {

    public static final String SESSION_USER = "user";

    private Auth() {
    }

    public static User currentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute(SESSION_USER);
    }
}
