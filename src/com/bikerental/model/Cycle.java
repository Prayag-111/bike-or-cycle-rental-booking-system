package com.bikerental.model;

/**
 * Cycle.java
 * Phase 2 - Structured Programming and Reliability
 */
public class Cycle {

    private String cycleId;
    private String cycleName;
    private String type;
    private double ratePerHour;
    private boolean isAvailable;

    public Cycle() {
        this.isAvailable = true;
    }

    public Cycle(String cycleId, String cycleName, String type, double ratePerHour) {
        this.cycleId = cycleId;
        this.cycleName = cycleName;
        this.type = type;
        this.ratePerHour = ratePerHour;
        this.isAvailable = true;
    }

    public String getCycleId() { return cycleId; }
    public void setCycleId(String cycleId) { this.cycleId = cycleId; }

    public String getCycleName() { return cycleName; }
    public void setCycleName(String cycleName) { this.cycleName = cycleName; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getRatePerHour() { return ratePerHour; }
    public void setRatePerHour(double ratePerHour) { this.ratePerHour = ratePerHour; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return "Cycle{" +
                "cycleId='" + cycleId + '\'' +
                ", cycleName='" + cycleName + '\'' +
                ", type='" + type + '\'' +
                ", ratePerHour=" + ratePerHour +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
