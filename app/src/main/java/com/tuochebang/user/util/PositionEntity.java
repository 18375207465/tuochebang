package com.tuochebang.user.util;

public class PositionEntity {
    public String address;
    public String city;
    public double latitue;
    public double longitude;

    public PositionEntity(double latitude, double longtitude, String address, String city) {
        this.latitue = latitude;
        this.longitude = longtitude;
        this.address = address;
    }
}
