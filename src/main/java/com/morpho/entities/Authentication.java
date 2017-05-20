package com.morpho.entities;

/**
 * Created by Gabriel on 5/20/2017.
 */
public class Authentication {
    private String userID;
    private String accessToken;

    public Authentication() {};
    public Authentication(String userID, String accessToken) {
        this.userID = userID;
        this.accessToken = accessToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String toString() {
        return "{userID: " + userID + ", accessToken: " + accessToken + "}";
    }
}
