package com.morpho.resources;

import com.morpho.MorphoApplication;
import com.morpho.views.ViewCreator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Irvin Umana on 29/4/2017.
 * Sets up the resources to obtain a certain page. Mostly HTML passed as a string.
 */
@Path("/")
@Produces(MediaType.TEXT_HTML)
public class AppResourcesPages {
    ViewCreator viewCreator;


    public AppResourcesPages(){
        viewCreator = new ViewCreator();
    }

    /**
     * Gets the createPiece page or view.
     * @return HTML for the createPiece page as a string.
     */
    @GET
    @Path("createPiece")
    public String getCreatePiecePage(){
        return viewCreator.getCreatePiecePage();
    }

    /**
     * Gets the editPiece page or view.
     * @return HTML for the editPiece page as a string.
     */
    @GET
    @Path("editPiece")
    public String getEditPiecePage(@QueryParam("pieceId") String pieceId ){

        return viewCreator.getEditPiecePage(pieceId);
    }



    @GET
    @Path("")
    public String getHomepage() { return viewCreator.getHomepage(); }

    @GET
    @Path("searchResults")
    public String getSearchResults(@QueryParam("searchJSON") String json)
    {
        return viewCreator.getResults(json);
    }

    @POST
    @Path("search")
    public String search(@FormParam("searchType") String searchType)
    {
        return viewCreator.search(searchType);
    }

    @GET
    @Path("profile")
    public String getProfile(@QueryParam("userType") String userType, @QueryParam("access_token") String accessToken, @QueryParam("userId") String userId)
    {
        if (userType.equals("facebook"))
            return viewCreator.getProfileUsingFacebook(accessToken, userId);
        else if (userType.equals("gmail")) {
            MorphoApplication.logger.info("user type: " + userType);
            return viewCreator.getProfileUsingGmail(accessToken);
        }
        else {
            MorphoApplication.logger.warning("Unable to determine user type.");
            return "ERROR";
        }
    }
}
