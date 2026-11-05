package com.bikerental.servlet;

import com.bikerental.service.BookingOperationsImpl;
import com.bikerental.service.UserOperationsImpl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * AppListener.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Runs once when the web application starts. It creates the shared objects that
 * every servlet and page uses: the user operations and the booking operations.
 */
@WebListener
public class AppListener implements ServletContextListener {

    public static final String USER_OPS = "userOps";
    public static final String BOOKING_OPS = "bookingOps";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        event.getServletContext().setAttribute(USER_OPS, new UserOperationsImpl());
        event.getServletContext().setAttribute(BOOKING_OPS, new BookingOperationsImpl());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().removeAttribute(USER_OPS);
        event.getServletContext().removeAttribute(BOOKING_OPS);
    }
}
