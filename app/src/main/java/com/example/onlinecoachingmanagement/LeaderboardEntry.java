package com.example.onlinecoachingmanagement;

public class LeaderboardEntry {
    private double percentage;

    // Default Constructor (Required for Firebase)
    public LeaderboardEntry() { }

    // Parameterized Constructor
    public LeaderboardEntry(double percentage) {
        this.percentage=percentage;
    }

    // Getters and Setters

    public double getScorePercentage() {
        return percentage;
    }
}
