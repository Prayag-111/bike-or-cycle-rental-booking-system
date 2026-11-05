package com.bikerental.service;

import com.bikerental.model.User;
import com.bikerental.exception.InvalidUserDataException;
import com.bikerental.exception.AuthenticationException;

/**
 * UserOperations.java
 * Phase 2 - Structured Programming and Reliability
 *
 * Interface defining the contract for user-related operations.
 * Any class that manages users (registration, login) must implement this.
 */
public interface UserOperations {

    /**
     * Validates and registers a new user.
     * @throws InvalidUserDataException if any field fails validation.
     */
    void registerUser(User user) throws InvalidUserDataException;

    /**
     * Authenticates a user against stored credentials.
     * @throws AuthenticationException if the username/password do not match.
     */
    User authenticateUser(String username, String password) throws AuthenticationException;
}
