/**
 * Created by Ricardo on 29/4/2017.
 */
package com.morpho;

import com.morpho.config.MorphoConfiguration;
import com.morpho.resources.AppResourcesMethods;
import com.morpho.resources.AppResourcesPages;
import com.morpho.server.DBAdministrator;
import com.morpho.health.MongoHealthCheck;
import io.dropwizard.assets.AssetsBundle;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.Application;

import com.mongodb.MongoClient;

public class MorphoApplication extends Application<MorphoConfiguration>{
    public static DBAdministrator DBA;
    public static Authenticator Auth;

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
        AppResourcesMethods resourcesMethods = new AppResourcesMethods();

        final MongoClient mongoClient = new MongoClient(configuration.mongohost, configuration.mongoport);
        final MongoHealthCheck mongoHealthCheck = new MongoHealthCheck(mongoClient);

        DBA = new DBAdministrator(mongoClient);
        Auth = new Authenticator();

        environment.healthChecks().register("MongoDBHealthCheck", mongoHealthCheck);
        environment.jersey().register(resourcesPages);
        environment.jersey().register(resourcesMethods);

        //dbaExample(DBA); //ejemplo
    }

    /*Ejemplo de consultas
    private void dbaExample(DBAdministrator dba) {
        dba.insert("pieza", "{_id: 1, ownerID: 21, isPublic: true, size: {w: 100, h: 100}, site: \"foo.com/pic.png\"}");
        dba.update("pieza", "{_id: 1}", "{$set: {isPublic: false}, $rename: {site: \"url\"}}"); //hay que usar update operators
        dba.delete("pieza", "{_id: 1}");
    }*/
}
