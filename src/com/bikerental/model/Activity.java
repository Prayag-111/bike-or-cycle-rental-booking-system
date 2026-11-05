package com.bikerental.model;

/**
 * Activity.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * One entry in the live activity feed, for example "booked Hero Cityline for 2 hours".
 */
public final class Activity {

    private final long timeMillis;
    private final String username;
    private final String message;

    public Activity(long timeMillis, String username, String message) {
        this.timeMillis = timeMillis;
        this.username = username;
        this.message = message;
    }

    public long getTimeMillis() { return timeMillis; }
    public String getUsername() { return username; }
    public String getMessage() { return message; }
}
