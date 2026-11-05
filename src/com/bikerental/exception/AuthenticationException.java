package com.bikerental.exception;

/**
 * AuthenticationException.java
 * Phase 2 - Structured Programming and Reliability
 *
 * Thrown when login credentials do not match a registered user.
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }
}
