package com.server;

import com.mongodb.DB;
import com.mongodb.MongoClient;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class DBAdministrator {
        Object mongoClient;
        DB db;
    public DBAdministrator(){
        mongoClient = new MongoClient( "localhost" , 27017 ).getDatabase( "Inge1" );
    }
}

