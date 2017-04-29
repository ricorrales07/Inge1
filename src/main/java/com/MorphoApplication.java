/**
 * Created by Ricardo on 29/4/2017.
 */
package com;

import com.resources.AppResourcesPages;
import com.server.DBAdministrator;
import com.health.MongoHealthCheck;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.Application;

import com.mongodb.MongoClient;

public class MorphoApplication extends Application<MorphoConfiguration>{
    public static void main(String[] args) throws Exception {
        new MorphoApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<MorphoConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(MorphoConfiguration configuration,
                    Environment environment) {
        AppResourcesPages resourcesPages = new AppResourcesPages();
        final MongoClient mongoClient = new MongoClient(configuration.mongohost, configuration.mongoport);
        final MongoHealthCheck healthCheck =
                new MongoHealthCheck(mongoClient);
        /*final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .using(environment)
                .build("SoyYo");*/
        DBAdministrator dba = new DBAdministrator(mongoClient);
        environment.healthChecks().register("MongoDBHealthCheck", healthCheck);
        environment.jersey().register(resourcesPages);
    }
}
