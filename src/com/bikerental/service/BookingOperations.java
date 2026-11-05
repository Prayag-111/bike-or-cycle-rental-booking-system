package com.bikerental.service;

import com.bikerental.exception.BookingException;
import com.bikerental.model.Activity;
import com.bikerental.model.Booking;
import com.bikerental.model.Cycle;
import com.bikerental.model.User;

import java.util.List;

/**
 * BookingOperations.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Interface for renting cycles. Servlets and pages use this interface and
 * do not need to know how cycles and bookings are stored.
 */
public interface BookingOperations {

    /** Returns every cycle with its current availability. */
    List<Cycle> getAllCycles();

    /** Books an available cycle for the given number of hours. */
    Booking bookCycle(User user, String cycleId, int hours) throws BookingException;

    /** Cancels one of the user's own bookings and frees the cycle. */
    void cancelBooking(User user, String bookingId) throws BookingException;

    /** Returns the active bookings that belong to one user. */
    List<Booking> getBookingsFor(String username);

    /** Returns the time (milliseconds since 1970) when a booking ends, or 0 if unknown. */
    long getEndTimeMillis(String bookingId);

    /** Returns the latest booking events from all users, newest first. */
    List<Activity> getRecentActivity();
}
