package com.example.potholedetectionappv1;

public class PotholeClass {
    String Date, Time, Address, Severity;
    private boolean expandable;
    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public PotholeClass(String date, String time, String address, String severity) {
        this.Date = date;
        this.Time = time;
        this.Address = address;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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
                ", Address='" + Address + '\'' +
                ", Severity='" + Severity + '\'' +
                ", expandable=" + expandable +
                '}';
    }

}
