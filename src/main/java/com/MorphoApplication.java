/**
 * Created by Ricardo on 29/4/2017.
 */
package com;
import com.MorphoConfiguration;
import com.resources.AppResourcesPages;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import sun.net.www.http.HttpClient;

import javax.ws.rs.client.Client;

public class MorphoApplication extends io.dropwizard.Application<MorphoConfiguration>{
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

        final Client client = new JerseyClientBuilder(environment).using(configuration.getJerseyClientConfiguration())
                .using(environment)
                .build("SoyYo");

        AppResourcesPages resourcesPages = new AppResourcesPages();
        environment.jersey().register(resourcesPages);
       // environment.addResource(new ExternalServiceResource(client));


    }
}
