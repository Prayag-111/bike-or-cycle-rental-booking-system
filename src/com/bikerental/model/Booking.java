package com.bikerental.model;

/**
 * Booking.java
 * Phase 2 - Structured Programming and Reliability
 */
public class Booking {

    private String bookingId;
    private User user;
    private Cycle cycle;
    private int durationInHours;
    private String bookingDate;

    public Booking() {
    }

    public Booking(String bookingId, User user, Cycle cycle, int durationInHours, String bookingDate) {
        this.bookingId = bookingId;
        this.user = user;
        this.cycle = cycle;
        this.durationInHours = durationInHours;
        this.bookingDate = bookingDate;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Cycle getCycle() { return cycle; }
    public void setCycle(Cycle cycle) { this.cycle = cycle; }

    public int getDurationInHours() { return durationInHours; }
    public void setDurationInHours(int durationInHours) { this.durationInHours = durationInHours; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public double calculateCost() {
        if (cycle == null || durationInHours <= 0) {
            return 0.0;
        }
        return cycle.getRatePerHour() * durationInHours;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", cycle=" + (cycle != null ? cycle.getCycleName() : "null") +
                ", durationInHours=" + durationInHours +
                ", bookingDate='" + bookingDate + '\'' +
                '}';
    }
}
