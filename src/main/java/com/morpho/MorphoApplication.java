/**
 * Created by Ricardo on 29/4/2017.
 */
package com.morpho;

import com.morpho.config.MorphoConfiguration;
import com.morpho.resources.AppResourcesPages;
import com.morpho.server.DBAdministrator;
import com.morpho.health.MongoHealthCheck;
import io.dropwizard.assets.AssetsBundle;

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
        return "morpho";
    }

    @Override
    public void initialize(Bootstrap<MorphoConfiguration> theConfig) {
        // nothing to do yet
        theConfig.addBundle(new AssetsBundle("/assets"));
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