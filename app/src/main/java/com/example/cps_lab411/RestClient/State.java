package com.example.cps_lab411.RestClient;

public class State {
    private int ModeCloud = 0;
    private  int Armed = 0;
    private  int Battery = 0;
    private int Connected = -1;

    public State() {
    }

    public int getModeCloud() {
        return ModeCloud;
    }

    public void setModeCloud(int modeCloud) {
        ModeCloud = modeCloud;
    }

    public int getArmed() {
        return Armed;
    }

    public void setArmed(int armed) {
        Armed = armed;
    }

    public int getBattery() {
        return Battery;
    }

    public void setBattery(int battery) {
        Battery = battery;
    }

    public int getConnected() {
        return Connected;
    }

    public void setConnected(int connected) {
        Connected = connected;
    }

    public static final State state = new State();
    public static State getInstance()
    {
        return state;
    }
}
