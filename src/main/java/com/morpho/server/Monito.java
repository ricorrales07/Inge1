package com.morpho.server;

import org.json.simple.JSONObject;

/**
 * Created by irvin on 10/9/17.
 * I am a Monito class (Monkey), and I do all the hard work.
 * THOUGH, I have no business dealing with responses, authentication and restful resources,
 * like, seriously guys.
 */
public class Monito {

    DBAdministrator dbA;

    public Monito(DBAdministrator dbA){
        this.dbA = dbA;
    }

    public void addNewUser(String userId,
                             String baseAccount,
                             String name,
                             String telephone,
                             String institution,
                             String email){
        JSONObject userToAdd = new JSONObject();
        userToAdd.put("userId", userId);
        userToAdd.put("baseAccount", baseAccount);
        userToAdd.put("name",name);
        userToAdd.put("telephone", telephone);
        userToAdd.put("institution",institution);
        userToAdd.put("email", email);

        try{
            dbA.insert("User", userToAdd.toJSONString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void logIn(String userId){



    }


}
