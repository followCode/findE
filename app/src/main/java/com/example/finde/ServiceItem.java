package com.example.finde;

import com.google.android.gms.maps.model.Marker;

public class ServiceItem {
    String name;
    String distance;
    String phoneNumber;
    String latLong;
    Marker marker;

    public ServiceItem(String name, String distance, String phoneNumber, String latLong) {
        this.name = name;
        this.distance = distance;
        this.phoneNumber = phoneNumber;
        this.latLong = latLong;
    }

    public void setMarker(Marker marker){this.marker = marker;}
    public Marker getMarker(){ return marker; }
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
