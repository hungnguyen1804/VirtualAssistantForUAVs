package com.example.cps_lab411.Category;

public class CategoryMode {
    private String flightMode;
    private int flightModeID;
    private int modeCommandID;

    public CategoryMode(String flightMode, int flightModeID, int modeCommandID) {
        this.flightMode = flightMode;
        this.flightModeID = flightModeID;
        this.modeCommandID = modeCommandID;
    }

    public String getName() {
        return flightMode;
    }
    public int getFlightModeID() { return flightModeID; }

    public void setName(String flightMode) {
        this.flightMode = flightMode;
    }

    public int getModeCommandID() {
        return modeCommandID;
    }

    public void setModeCommandID(int modeCommandID) {
        this.modeCommandID = modeCommandID;
    }
}
