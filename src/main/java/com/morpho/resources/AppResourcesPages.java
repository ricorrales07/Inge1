package com.morpho.resources;

import com.morpho.views.ViewCreator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    @Path("sample")
    public String getSamplePage() {
        return viewCreator.getSamplePage();
    }

    @GET
    @Path("createPiece")
    public String getCreatePiecePage(){
        return viewCreator.getCreatePiecePage();
    }

    @GET
    @Path("editPiece")
    public String getCreatePiecePage(@QueryParam("pieceId") String pieceId ){

        return viewCreator.getEditPiecePage(pieceId);
    }



    @GET
    @Path("")
    public String getHomepage() { return viewCreator.getHomepage(); }
}
