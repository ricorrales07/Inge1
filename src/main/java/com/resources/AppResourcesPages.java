package com.resources;

import com.ViewCreator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Ricardo on 29/4/2017.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResourcesPages {
    ViewCreator viewCreator;
    public AppResourcesPages(){
        viewCreator = new ViewCreator();
    }

    @GET
    @Path("login")
    public String getLoginPage() {
        return viewCreator.getLoginPage();
    }

}
