package com.example.karat.fksc.models;

public class CoverPhoto {

    private String cover_img_url;

    public CoverPhoto(String cover_img_url) {
        this.cover_img_url = cover_img_url;
    }


    public CoverPhoto() {

    }

    public String getCover_img_url() {
        return cover_img_url;
    }

    public void setCover_img_url(String cover_img_url) {
        this.cover_img_url = cover_img_url;
    }

    @Override
    public String toString() {
        return "CoverPhoto{" +
                "cover_img_url='" + cover_img_url + '\'' +
                '}';
    }
}
