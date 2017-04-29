package com.health;

import com.mongodb.MongoClient;
import com.codahale.metrics.health.HealthCheck;
/**
 * Created by Gabriel on 4/29/2017.
 */

public class MongoHealthCheck extends HealthCheck {
    private final MongoClient mongoClient;

    public MongoHealthCheck(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    protected Result check() throws Exception {
        mongoClient.getDatabaseNames();
        return Result.healthy();
    }
}