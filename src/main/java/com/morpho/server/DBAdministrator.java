package com.morpho.server;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
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
    public void insert(String collection, String document) throws Exception{
        db.getCollection(collection).insertOne(Document.parse(document));
    }

    /**
     * Inserts document in serialized JSON into collection, overriding existing document
     * with same id
     * @param collection name of collection
     * @param document string in serialized JSON
     */
    public void set(String collection, String document) throws Exception{
        try {
            db.getCollection(collection).insertOne(Document.parse(document));
        } catch(MongoWriteException e) {
            System.out.println(e);
            if(e.getCode() == 11000) { //duplicate key error
                delete(collection, "{_id: \"" + Document.parse(document).get("_id") + "\"}");
                db.getCollection(collection).insertOne(Document.parse(document));
            }
        }
    }

    /**
     * Updates document in collection
     * @param collection name of collection
     * @param filter string in serialized JSON
     * @param update string in serialized JSON
     */
    public void update(String collection, String filter, String update) throws Exception {
        db.getCollection(collection).updateOne((Bson) JSON.parse(filter), (Bson) JSON.parse(update));
    }

    /**
     * Finds document in collection
     * @param collection name of collection
     * @param filter string in serialized JSON
     * @return document in serialized JSON
     */
    public String find(String collection, String filter) throws Exception {
        try {
            return db.getCollection(collection).find((Bson) JSON.parse(filter)).first().toJson();
        } catch (Exception e) {
            return null;
        }
    }

    public FindIterable<Document> search(String collection, String filter) throws Exception {
        try {
            BasicDBObject f = BasicDBObject.parse(filter);
            FindIterable<Document> result =
                db.getCollection(collection).find(f);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deletes documents matching filter
     * @param collection name of collection
     * @param filter string in serialized JSON
     */
    public void delete(String collection, String filter) throws Exception{
        db.getCollection(collection).deleteMany((Bson) JSON.parse(filter));
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        client.close();
    }
}

