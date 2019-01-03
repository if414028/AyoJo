package com.jo.ayo.ayojo.data.model;

public class PostData {
    private String houseOwner;
    private String firstAnsewr;
    private String secondAnswer;
    private float lat;
    private float lng;

    public String getHouseOwner() {
        return houseOwner;
    }

    public void setHouseOwner(String houseOwner) {
        this.houseOwner = houseOwner;
    }

    public String getFirstAnsewr() {
        return firstAnsewr;
    }

    public void setFirstAnsewr(String firstAnsewr) {
        this.firstAnsewr = firstAnsewr;
    }

    public String getSecondAnswer() {
        return secondAnswer;
    }

    public void setSecondAnswer(String secondAnswer) {
        this.secondAnswer = secondAnswer;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
