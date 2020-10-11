package com.example.finde;

public class ServiceItem {
    String name;
    String distance;
    String phoneNumber;
    String latLong;

    public ServiceItem(String name, String distance, String phoneNumber, String latLong) {
        this.name = name;
        this.distance = distance;
        this.phoneNumber = phoneNumber;
        this.latLong = latLong;
    }

    public String getName() {
        return name;
    }
    public String getDistance() {
        return distance;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getLatLong() {
        return latLong;
    }
}
