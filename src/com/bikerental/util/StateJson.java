package com.bikerental.util;

import com.bikerental.model.Activity;
import com.bikerental.model.Booking;
import com.bikerental.model.Cycle;
import com.bikerental.model.User;
import com.bikerental.service.BookingOperations;

import java.util.List;
import java.util.Locale;

/**
 * StateJson.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Builds the JSON "state" that the dashboard shows: the cycles, the logged-in user's
 * bookings, and the live activity feed. The page asks for it every few seconds.
 */
public final class StateJson {

    private StateJson() {
    }

    public static String build(BookingOperations ops, User user, long now) {

        List<Cycle> cycles = ops.getAllCycles();
        List<Booking> mine = ops.getBookingsFor(user.getUsername());
        List<Activity> events = ops.getRecentActivity();

        int available = 0;
        for (Cycle c : cycles) {
            if (c.isAvailable()) {
                available++;
            }
        }

        StringBuilder sb = new StringBuilder(1024);
        sb.append("{\"serverTime\":").append(now);
        sb.append(",\"user\":{\"username\":").append(Json.quote(user.getUsername()))
          .append(",\"fullName\":").append(Json.quote(user.getFullName())).append('}');
        sb.append(",\"availableCount\":").append(available);
        sb.append(",\"totalCount\":").append(cycles.size());

        sb.append(",\"cycles\":[");
        for (int i = 0; i < cycles.size(); i++) {
            Cycle c = cycles.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"id\":").append(Json.quote(c.getCycleId()))
              .append(",\"name\":").append(Json.quote(c.getCycleName()))
              .append(",\"type\":").append(Json.quote(c.getType()))
              .append(",\"rate\":").append(String.format(Locale.ROOT, "%.2f", c.getRatePerHour()))
              .append(",\"available\":").append(c.isAvailable()).append('}');
        }

        sb.append("],\"bookings\":[");
        for (int i = 0; i < mine.size(); i++) {
            Booking b = mine.get(i);
            if (i > 0) sb.append(',');
            sb.append("{\"id\":").append(Json.quote(b.getBookingId()))
              .append(",\"cycle\":").append(Json.quote(b.getCycle().getCycleName()))
              .append(",\"hours\":").append(b.getDurationInHours())
              .append(",\"total\":").append(String.format(Locale.ROOT, "%.2f", b.calculateCost()))
              .append(",\"bookedAt\":").append(Json.quote(b.getBookingDate()))
              .append(",\"endsAt\":").append(ops.getEndTimeMillis(b.getBookingId())).append('}');
        }

        // Other people are shown as "Another rider" so usernames are not exposed
        sb.append("],\"activity\":[");
        for (int i = 0; i < events.size(); i++) {
            Activity a = events.get(i);
            if (i > 0) sb.append(',');
            String who = a.getUsername().equals(user.getUsername()) ? "You" : "Another rider";
            sb.append("{\"who\":").append(Json.quote(who))
              .append(",\"text\":").append(Json.quote(a.getMessage()))
              .append(",\"time\":").append(a.getTimeMillis()).append('}');
        }
        sb.append("]}");
        return sb.toString();
    }
}
