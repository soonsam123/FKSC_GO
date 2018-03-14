package com.example.karat.fksc.models;

/**
 * Created by karat on 14/03/2018.
 */

public class UserAndUserSettings {

    private User user;
    private UserSettings userSettings;

    public UserAndUserSettings(User user, UserSettings userSettings) {
        this.user = user;
        this.userSettings = userSettings;
    }

    public UserAndUserSettings() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    @Override
    public String toString() {
        return "UserAndUserSettings{" +
                "user=" + user +
                ", userSettings=" + userSettings +
                '}';
    }
}
