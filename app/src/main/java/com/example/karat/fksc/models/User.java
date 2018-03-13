package com.example.karat.fksc.models;

/**
 * Created by karat on 09/03/2018.
 */

public class User {

    private String full_name;
    private String birth_date;
    private String belt_color;
    private String registration_number;
    private String dojo;
    private String user_id;
    private boolean verified;

    public User(String full_name, String birth_date, String belt_color, String registration_number, String dojo, String user_id, boolean verified) {
        this.full_name = full_name;
        this.birth_date = birth_date;
        this.belt_color = belt_color;
        this.registration_number = registration_number;
        this.dojo = dojo;
        this.user_id = user_id;
        this.verified = verified;
    }

    public User() {

    }


    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getBelt_color() {
        return belt_color;
    }

    public void setBelt_color(String belt_color) {
        this.belt_color = belt_color;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getDojo() {
        return dojo;
    }

    public void setDojo(String dojo) {
        this.dojo = dojo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
                "full_name='" + full_name + '\'' +
                ", birth_date='" + birth_date + '\'' +
                ", belt_color='" + belt_color + '\'' +
                ", registration_number=" + registration_number +
                ", dojo='" + dojo + '\'' +
                ", user_id='" + user_id + '\'' +
                ", verified=" + verified +
                '}';
    }
}
