package com.bikerental.exception;

/**
 * BookingException.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Thrown when a booking or cancellation is not allowed
 * (cycle already booked, invalid hours, booking not found, and so on).
 */
public class BookingException extends Exception {

    public BookingException(String message) {
        super(message);
    }
}
