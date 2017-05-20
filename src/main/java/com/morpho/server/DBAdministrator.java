package com.morpho.server;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.lifecycle.Managed;
import com.mongodb.MongoClient;
import org.bson.Document;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class DBAdministrator implements Managed {
    private final MongoClient client;
    private final MongoDatabase db;

    public DBAdministrator(MongoClient client) {
        this.client = client;
        this.db = client.getDatabase("MorphoDB");
    }

    /**
     * Inserts document in serialized JSON into collection
     * @param collection name of collection
     * @param document string in serialized JSON
     */
    public void insert(String collection, String document) {
        db.getCollection(collection).insertOne(Document.parse(document));
    }

    /**
     * Updates document in collection
     * @param collection name of collection
     * @param filter string in serialized JSON
     * @param update string in serialized JSON
     */
    public void update(String collection, String filter, String update) {
        db.getCollection(collection).updateOne((Bson) JSON.parse(filter), (Bson) JSON.parse(update));
    }

    /**
     * Finds document in collection
     * @param collection name of collection
     * @param filter string in serialized JSON
     * @return document in serialized JSON
     */
    public String find(String collection, String filter) {
        return db.getCollection(collection).find((Bson) JSON.parse(filter)).first().toJson();
    }

    /**
     * Deletes documents matching filter
     * @param collection name of collection
     * @param filter string in serialized JSON
     */
    public void delete(String collection, String filter) {
        db.getCollection(collection).deleteMany((Bson) JSON.parse(filter));
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        client.close();
    }
}

