package com.morpho.entities;

/**
 * Created by Gabriel on 5/20/2017.
 */
public class Authentication {
    private String userID;
    private String accessToken;

    public Authentication(String userID, String accessToken) {
        this.userID = userID;
        this.accessToken = accessToken;
    }

    public String toString() {
        return "{_id: \"" + userID + "\", accessToken: \"" + accessToken + "\"}";
    }
}
