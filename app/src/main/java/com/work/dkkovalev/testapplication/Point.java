package com.work.dkkovalev.testapplication;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Point implements Serializable {

    private String title;
    private String description;
    private double lat;
    private double lng;
    private String id;

    public Point(String title, double lat, double lng) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
    }

    public Point(String title, String description, double lat, double lng) {
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
