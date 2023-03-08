package com.example.potholedetectionappv1;

public class PotholeClass {

    String Date, Time, Longitude, Latitude, Severity;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public PotholeClass(String date, String time, String longitude, String latitude, String severity) {
        this.Date = date;
        this.Time = time;
        this.Longitude = longitude;
        this.Latitude = latitude;
        this.Severity = severity;
        this.expandable = false;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getSeverity() {
        return Severity;
    }

    public void setSeverity(String severity) {
        Severity = severity;
    }

    @Override
    public String toString() {
        return "PotholeClass{" +
                "Date='" + Date + '\'' +
                ", Time='" + Time + '\'' +
                ", Longitude='" + Longitude + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", Severity='" + Severity + '\'' +
                ", expandable=" + expandable +
                '}';
    }

}
