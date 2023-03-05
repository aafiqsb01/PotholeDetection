package com.example.potholedetectionappv1;

public class PotholeClass {

    String Date, Time, Longitude, Latitude;
    boolean isVisible;

    public PotholeClass(String date, String time, String longitude, String latitude, boolean isVisible) {
        this.Date = date;
        this.Time = time;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.isVisible = isVisible;
    }
}
