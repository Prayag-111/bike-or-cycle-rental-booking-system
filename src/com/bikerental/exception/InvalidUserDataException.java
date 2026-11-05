package com.bikerental.exception;

/**
 * InvalidUserDataException.java
 * Phase 2 - Structured Programming and Reliability
 *
 * Thrown when user-provided data (username, password, email, phone)
 * fails validation during registration.
 */
public class InvalidUserDataException extends Exception {

    public InvalidUserDataException(String message) {
        super(message);
    }
}
