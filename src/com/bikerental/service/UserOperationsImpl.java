package com.bikerental.service;

import com.bikerental.model.User;
import com.bikerental.exception.InvalidUserDataException;
import com.bikerental.exception.AuthenticationException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * UserOperationsImpl.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Same interface as Phase 2, with changes needed once a web server calls it:
 *  - thread-safe storage (many requests can arrive at the same time)
 *  - null-safe checks (a request can leave any field out)
 *  - the full name is validated here too, because browser checks can be bypassed
 */
public class UserOperationsImpl implements UserOperations {

    private final Map<String, User> registeredUsers = new ConcurrentHashMap<>();

    @Override
    public synchronized void registerUser(User user) throws InvalidUserDataException {

        if (user == null) {
            throw new InvalidUserDataException("No user data received.");
        }

        String fullName = user.getFullName();
        if (fullName == null || !fullName.trim().matches("^[A-Za-z][A-Za-z .'-]{2,}$")) {
            throw new InvalidUserDataException("Full name must be at least 3 characters, letters and spaces only.");
        }

        String username = user.getUsername() == null ? null : user.getUsername().trim();
        if (username == null || !username.matches("^[a-zA-Z0-9_]{4,}$")) {
            throw new InvalidUserDataException("Username must be at least 4 characters (letters, digits, underscore only).");
        }

        String password = user.getPassword();
        if (password == null || password.length() < 6
                || !password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            throw new InvalidUserDataException("Password must be at least 6 characters with a letter and a digit.");
        }

        if (user.getEmail() == null || !user.getEmail().trim().matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new InvalidUserDataException("Email format is invalid.");
        }

        if (user.getPhoneNumber() == null || !user.getPhoneNumber().trim().matches("^[0-9]{10}$")) {
            throw new InvalidUserDataException("Phone number must be exactly 10 digits.");
        }

        if (registeredUsers.containsKey(username)) {
            throw new InvalidUserDataException("Username already exists.");
        }

        registeredUsers.put(username, user);
    }

    @Override
    public User authenticateUser(String username, String password) throws AuthenticationException {

        if (username == null || password == null) {
            throw new AuthenticationException("Invalid username or password.");
        }

        User user = registeredUsers.get(username.trim());

        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid username or password.");
        }

        return user;
    }
}
