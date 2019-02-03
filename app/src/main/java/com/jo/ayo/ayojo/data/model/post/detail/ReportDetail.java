package com.jo.ayo.ayojo.data.model.post.detail;

public class ReportDetail {
    private String id;
    private String address;
    private String lat;
    private String lng;
    private String images;
    private String createdAt;
    private String updatedAt;
    private String OtherSurveyorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOtherSurveyorId() {
        return OtherSurveyorId;
    }

    public void setOtherSurveyorId(String otherSurveyorId) {
        OtherSurveyorId = otherSurveyorId;
    }
}
