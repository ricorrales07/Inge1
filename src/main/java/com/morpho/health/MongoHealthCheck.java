package com.morpho.health;

import com.mongodb.MongoClient;
import com.codahale.metrics.health.HealthCheck;
import com.morpho.MorphoApplication;

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
        StringBuilder dbNames = new StringBuilder("dbs: ");
        try {
            for(String doc : mongoClient.listDatabaseNames()) {
                dbNames.append(doc).append(", ");
            }
        } catch(Exception mongoError) {
            MorphoApplication.logger.info(mongoError.toString());
            return Result.unhealthy(mongoError);
        }
        return Result.healthy(dbNames.toString());
    }
}