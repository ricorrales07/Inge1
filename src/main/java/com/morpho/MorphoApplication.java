package com.morpho;

import com.morpho.config.MorphoConfiguration;
import com.morpho.resources.AppResourcesMethods;
import com.morpho.resources.AppResourcesPages;
import com.morpho.server.DBAdministrator;
import com.morpho.health.MongoHealthCheck;
import com.morpho.server.SearchEngine;
import io.dropwizard.assets.AssetsBundle;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.Application;

import com.mongodb.MongoClient;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.*;

/**
 * Created by Irvin Umaña on 29/4/2017.
 * General configuration class for the entire application running dropwizard.
 */
public class MorphoApplication extends Application<MorphoConfiguration>{
    public static DBAdministrator DBA;
    public static Authenticator Auth;
    public static SearchEngine searcher;

    public static Logger logger;
    private static FileHandler fHandler;
    private static SimpleFormatter sFormatter;

    public static void main(String[] args) throws Exception {
        fHandler = new FileHandler("MorphoSpace.log");
        sFormatter = new SimpleFormatter();
        logger = Logger.getLogger("MorphoApplication");

        fHandler.setFormatter(sFormatter);
        logger.addHandler(fHandler);
        logger.setLevel(Level.INFO);

        new MorphoApplication().run(args);
    }

    @Override
    public String getName() {
        return "morpho";
    }

    /**
     * Setting up the assets folder as public to allow serving different resources such
     * as JavaScript, CSS, and Image files.
     * @param theConfig configuration object generated by the config.yml.
     */
    @Override
    public void initialize(Bootstrap<MorphoConfiguration> theConfig) {

        theConfig.addBundle(new AssetsBundle("/assets"));
    }

    /**
     * Begin executing the application. Sets registration of different resources,
     * and sets up configurations for the database and the authentication.
     * @param configuration configuration object generated by the config.yml.
     * @param environment provided the by the Resftul environment.
     */
    @Override
    public void run(MorphoConfiguration configuration,
                    Environment environment) {

        final MongoClient mongoClient = new MongoClient(configuration.mongohost, configuration.mongoport);
        final MongoHealthCheck mongoHealthCheck = new MongoHealthCheck(mongoClient);

        final Driver neo4jDriver = GraphDatabase.driver(configuration.neo4jURI);

        DBA = new DBAdministrator(mongoClient, neo4jDriver);
        Auth = new Authenticator();
        searcher = new SearchEngine();

        AppResourcesPages resourcesPages = new AppResourcesPages();
        AppResourcesMethods resourcesMethods = new AppResourcesMethods();

        environment.healthChecks().register("MongoDBHealthCheck", mongoHealthCheck);
        environment.jersey().register(resourcesPages);
        environment.jersey().register(resourcesMethods);

        //dbaExample(DBA); //ejemplo
    }

    public static String getImageBytes(String src) {
        try {
            FileInputStream image = new FileInputStream(src);
            byte[] imageBytes = new byte[0];
            for (byte[] ba = new byte[image.available()];
                 image.read(ba) != -1; ) {
                byte[] baTmp = new byte[imageBytes.length + ba.length];
                System.arraycopy(imageBytes, 0, baTmp, 0, imageBytes.length);
                System.arraycopy(ba, 0, baTmp, imageBytes.length, ba.length);
                imageBytes = baTmp;
            }

            return DatatypeConverter.printBase64Binary(imageBytes);
        } catch (FileNotFoundException e) {
            logger.warning(e.toString());
            return "";
        } catch (IOException e) {
            logger.warning(e.toString());
            return "";
        }
    }

    //Ejemplo de consultas
    /*private void dbaExample(DBAdministrator dba) {
        System.out.println("Test!");
        dba.setRelationship("100126517238799", "100126517238799C12", "piece", "down");
        System.out.println(dba.findRelatedUsers("100126517238799C12", "piece", "down").toString());
        System.out.println(dba.findRelatedObjects("100126517238799", "piece", "down").toString());
    }*/
}
