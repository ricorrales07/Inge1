package com.server;

import io.dropwizard.lifecycle.Managed;
import com.mongodb.MongoClient;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class DBAdministrator implements Managed {
    private final MongoClient client;

    public DBAdministrator(MongoClient client) {
        this.client = client;
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        client.close();
    }
}

