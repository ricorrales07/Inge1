package com.morpho.server;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;
import com.morpho.MorphoApplication;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.lifecycle.Managed;
import com.mongodb.MongoClient;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Ricardo on 29/4/2017.
 */
public class DBAdministrator implements Managed {
    private final MongoClient client;
    private final MongoDatabase db;
    private final Session neo4jSession;

    public DBAdministrator(MongoClient client, Driver neo4jDriver) {
        this.client = client;
        this.db = client.getDatabase("MorphoDB");
        this.neo4jSession = neo4jDriver.session();
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
            MorphoApplication.logger.info(e.toString());
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
    public void replace(String collection, String filter, String update) throws Exception {
        db.getCollection(collection).replaceOne((Bson) JSON.parse(filter), Document.parse(update));
    }

    public void update(String collection, String filter, String update) throws Exception {
        db.getCollection(collection).updateOne((Bson) JSON.parse(filter), Document.parse(update));
        MorphoApplication.logger.info("Update completed. Collection: " + collection
                + ", filter: " + filter + ", update: " + update);
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
            MorphoApplication.logger.info(e.toString());
            return null;
        }
    }

    public Document documentFind(String collection, String filter) throws Exception {
        try {
            return db.getCollection(collection).find((Bson) JSON.parse(filter)).first();
        } catch (Exception e) {
            MorphoApplication.logger.info(e.toString());
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
            MorphoApplication.logger.info(e.toString());
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

    /**
     * Sets relationship in Neo4j DB between user and comp/piece
     * @param userID id of user
     * @param objectID id of comp or piece
     * @param type "composition" or "piece"
     * @param relation "up" or "down"
     */
    public void setRelationship(String userID, String objectID, String type, String relation) {
        String query = "MATCH (u:user {_id:\"" + userID + "\"}), (o:" + type + " {_id:\"" + objectID + "\"})" +
                "CREATE (u)<-[:" + type + "_" + relation + "]-(o)";
        try (Transaction tx = neo4jSession.beginTransaction())
        {
            tx.run(query);
            tx.success();
        }
    }

    /**
     * Finds users related to comp/piece
     * @param id id of piece/comp
     * @param type "composition" or "piece"
     * @param relation "up" or "down"
     * @return List of users in JSONObject format
     */
    public List<JSONObject> findRelatedUsers(String id, String type, String relation) {
        String query = "MATCH ({_id : \"" + id + "\"})-[:" + type + "_" + relation + "]->(n) RETURN n";
        return neo4jQuery(query, id);
    }

    /**
     * Finds comps/pieces related to users
     * @param user_id user id
     * @param type "composition" or "piece"
     * @param relation "up" or "down"
     * @return List of users in JSONObject format
     */
    public List<JSONObject> findRelatedObjects(String user_id, String type, String relation) {
        String query = "MATCH ({_id : \"" + user_id + "\"})<-[:" + type + "_" + relation + "]-(n) RETURN n";
        return neo4jQuery(query, user_id);
    }

    private List<JSONObject> neo4jQuery(String query, String id) {
        List<JSONObject> result = new ArrayList<>();
        try (Transaction tx = neo4jSession.beginTransaction())
        {
            StatementResult sr = tx.run(query);
            while ( sr.hasNext() )
            {
                Node nextNode = sr.next().get("n").asNode();
                JSONObject nodeAsJSON = new JSONObject(nextNode.asMap());
                if(!nodeAsJSON.get("_id").equals(id)) {
                    result.add(nodeAsJSON);
                }
            }
        }
        return result;
    }

    public void start() throws Exception {
    }

    public void stop() throws Exception {
        client.close();
    }
}

