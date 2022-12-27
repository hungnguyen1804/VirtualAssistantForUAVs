package com.example.cps_lab411.RestAPI;

public class GlobalPosition {
    private float Altitude = 0;
    private double Latitude = 21.0064388;
    private double Longitude = 105.8428835;
    private float Vx = 0;
    private float Vy = 0;
    private float Vz = 0;
    private float Yawrate = 0;

    public GlobalPosition() {
    }

    public float getAltitude() {
        return Altitude;
    }

    public void setAltitude(float altitude) {
        Altitude = altitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public float getVx() {
        return Vx;
    }

    public void setVx(float vx) {
        Vx = vx;
    }

    public float getVy() {
        return Vy;
    }

    public void setVy(float vy) {
        Vy = vy;
    }

    public float getVz() {
        return Vz;
    }

    public void setVz(float vz) {
        Vz = vz;
    }

    public float getYawrate() {
        return Yawrate;
    }

    public void setYawrate(float yawrate) {
        Yawrate = yawrate;
    }

    public static final GlobalPosition globalPosition = new GlobalPosition();
    public static GlobalPosition getInstance()
    {
        return globalPosition;
    }
}
