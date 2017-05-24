package com.morpho;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gabriel on 5/20/2017.
 */
public class Authenticator {
    private final int TIMEOUT = 10000;
    private final String appSecret = "7871d7fdd5ab855efeed83ebb343b250";
    private final String appID = "212006762635772";
    private final String appTokenURL = "https://graph.facebook.com/oauth/access_token?";
    private final String clientIDParam = "client_id=";
    private final String clientSecretParam = "&client_secret=";
    private final String grantTypeParam = "&grant_type=client_credentials";

    private final String verifyURL = "https://graph.facebook.com/debug_token?";
    private final String accessTokenParam = "input_token=";
    private final String appTokenParam = "&access_token=";

    public Authenticator() {};

    private String getResponseFromURL(String url) {
        HttpsURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpsURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(TIMEOUT);
            c.setReadTimeout(TIMEOUT);
            c.connect();
            int status = c.getResponseCode();
            System.out.println(url + "\nstatus: " + status);

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (MalformedURLException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        return null;
    }

    private String getAppToken() {
        String url = appTokenURL + clientIDParam + appID + clientSecretParam + appSecret + grantTypeParam;
        String receivedAppToken = getResponseFromURL(url);
        try {
            JSONObject appTokenJSON = (JSONObject) new JSONParser().parse(receivedAppToken);
            return (String) appTokenJSON.get("access_token");
        } catch(ParseException e) {
            System.out.println(e);
        }
        return null;
    }
    private String getUserID(String givenAccessToken, String appToken) {
        String url = verifyURL + accessTokenParam + givenAccessToken + appTokenParam + appToken;
        String tokenData = getResponseFromURL(url);
        try {
            JSONObject dataJSON = (JSONObject) new JSONParser().parse(tokenData);
            return (String) ((JSONObject) dataJSON.get("data")).get("user_id");
        } catch(ParseException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean verifyUser(String givenUserID, String givenAccessToken) {
        String appToken = getAppToken();
        if(givenUserID != null && appToken != null) {
            String userID = getUserID(givenAccessToken, appToken);
            return givenUserID.equals(userID);
        }
        return false;
    }
}
