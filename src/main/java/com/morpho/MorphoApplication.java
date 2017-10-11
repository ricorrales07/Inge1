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
        AppResourcesPages resourcesPages = new AppResourcesPages();
        AppResourcesMethods resourcesMethods = new AppResourcesMethods();

        final MongoClient mongoClient = new MongoClient(configuration.mongohost, configuration.mongoport);
        final MongoHealthCheck mongoHealthCheck = new MongoHealthCheck(mongoClient);

        DBA = new DBAdministrator(mongoClient);
        Auth = new Authenticator();
        searcher = new SearchEngine();

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

    /*Ejemplo de consultas
    private void dbaExample(DBAdministrator dba) {
        dba.insert("pieza", "{_id: 1, ownerID: 21, isPublic: true, size: {w: 100, h: 100}, site: \"foo.com/pic.png\"}");
        dba.replace("pieza", "{_id: 1}", "{$set: {isPublic: false}, $rename: {site: \"url\"}}"); //hay que usar update operators
        dba.delete("pieza", "{_id: 1}");
    }*/
}
