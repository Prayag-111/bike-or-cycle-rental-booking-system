package com.bikerental.servlet;

import com.bikerental.exception.BookingException;
import com.bikerental.model.Booking;
import com.bikerental.model.User;
import com.bikerental.service.BookingOperations;
import com.bikerental.util.Auth;
import com.bikerental.util.Json;
import com.bikerental.util.StateJson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ApiServlet.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * The JSON service that the live dashboard talks to (no page reloads):
 *   GET  /api/state  : current cycles, the user's bookings and the activity feed
 *   POST /api/book   : book a cycle         (cycleId, hours)
 *   POST /api/cancel : cancel a booking     (bookingId)
 * Every reply is JSON. Booking replies include the new state, so the page can update at once.
 */
@WebServlet("/api/*")
public class ApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        if (!"/state".equals(request.getPathInfo())) {
            send(response, 404, plain(false, "Not found."));
            return;
        }

        User user = Auth.currentUser(request);
        if (user == null) {
            send(response, 401, loggedOut());
            return;
        }

        BookingOperations ops =
                (BookingOperations) getServletContext().getAttribute(AppListener.BOOKING_OPS);
        send(response, 200, StateJson.build(ops, user, System.currentTimeMillis()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String path = request.getPathInfo();

        // Only the dashboard's own script sends this header, which blocks forms on other websites
        if (!"XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            send(response, 403, plain(false, "Request not allowed."));
            return;
        }

        User user = Auth.currentUser(request);
        if (user == null) {
            send(response, 401, loggedOut());
            return;
        }

        request.setCharacterEncoding("UTF-8");
        BookingOperations ops =
                (BookingOperations) getServletContext().getAttribute(AppListener.BOOKING_OPS);

        try {
            String message;

            if ("/book".equals(path)) {
                int hours = parseHours(request.getParameter("hours"));
                Booking booking = ops.bookCycle(user, request.getParameter("cycleId"), hours);
                message = "Booked " + booking.getCycle().getCycleName()
                        + " for " + hours + (hours == 1 ? " hour" : " hours")
                        + ". Total: Rs. " + String.format("%.0f", booking.calculateCost());

            } else if ("/cancel".equals(path)) {
                ops.cancelBooking(user, request.getParameter("bookingId"));
                message = "Booking cancelled. The cycle is available again.";

            } else {
                send(response, 404, plain(false, "Not found."));
                return;
            }

            send(response, 200, reply(true, message, ops, user));

        } catch (BookingException e) {
            send(response, 409, reply(false, e.getMessage(), ops, user));
        }
    }

    private static int parseHours(String text) throws BookingException {
        try {
            return Integer.parseInt(text.trim());
        } catch (RuntimeException e) {
            throw new BookingException("Choose a valid number of hours.");
        }
    }

    private static String reply(boolean ok, String message, BookingOperations ops, User user) {
        return "{\"ok\":" + ok + ",\"message\":" + Json.quote(message)
                + ",\"state\":" + StateJson.build(ops, user, System.currentTimeMillis()) + "}";
    }

    private static String plain(boolean ok, String message) {
        return "{\"ok\":" + ok + ",\"message\":" + Json.quote(message) + "}";
    }

    private static String loggedOut() {
        return "{\"ok\":false,\"loggedOut\":true,\"message\":\"Please log in again.\"}";
    }

    private static void send(HttpServletResponse response, int status, String json) throws IOException {
        response.setStatus(status);
        response.setHeader("Cache-Control", "no-store");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
