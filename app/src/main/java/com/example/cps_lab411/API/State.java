package com.example.cps_lab411.API;

import com.example.cps_lab411.DataHolder;

import org.json.JSONObject;

public class State {
    private String ModeCloud;
    private String Armed;
    private String Battery;
    private String Connected;

    public State() {
    }

    public String getModeCloud() {
        return ModeCloud;
    }

    public void setModeCloud(String modeCloud) {
        ModeCloud = modeCloud;
    }

    public String getArmed() {
        return Armed;
    }

    public void setArmed(String armed) {
        Armed = armed;
    }

    public String getBattery() {
        return Battery;
    }

    public void setBattery(String battery) {
        Battery = battery;
    }

    public String getConnected() {
        return Connected;
    }

    public void setConnected(String connected) {
        Connected = connected;
    }

    public static final State state = new State();
    public static State getInstance()
    {
        return state;
    }
}
