package com.example.karat.fksc.models;

/**
 * Created by karat on 14/03/2018.
 */

public class User {

    private String dojo;
    private String full_name;
    private String profile_img_url;
    private String registration_number;
    private boolean verified;

    public User(String dojo, String full_name, String profile_img_url, String registration_number, boolean verified) {
        this.dojo = dojo;
        this.full_name = full_name;
        this.profile_img_url = profile_img_url;
        this.registration_number = registration_number;
        this.verified = verified;
    }


    public User() {

    }

    public String getDojo() {
        return dojo;
    }

    public void setDojo(String dojo) {
        this.dojo = dojo;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
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
        return "User{" +
                "dojo='" + dojo + '\'' +
                ", full_name='" + full_name + '\'' +
                ", profile_img_url='" + profile_img_url + '\'' +
                ", registration_number='" + registration_number + '\'' +
                ", verified=" + verified +
                '}';
    }
}
