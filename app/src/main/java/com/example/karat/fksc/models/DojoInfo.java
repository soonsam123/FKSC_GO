package com.example.karat.fksc.models;

/**
 * Created by karat on 17/03/2018.
 */

public class DojoInfo {

    private String name;
    private String city;
    private String cover_img_url;
    private String registration_number;
    private boolean verified;

    public DojoInfo(String name, String city, String cover_img_url, String registration_number, boolean verified) {
        this.name = name;
        this.city = city;
        this.cover_img_url = cover_img_url;
        this.registration_number = registration_number;
        this.verified = verified;
    }


    public DojoInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCover_img_url() {
        return cover_img_url;
    }

    public void setCover_img_url(String cover_img_url) {
        this.cover_img_url = cover_img_url;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "DojoInfo{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", cover_img_url='" + cover_img_url + '\'' +
                ", registration_number='" + registration_number + '\'' +
                ", verified=" + verified +
                '}';
    }
}
