package com.morpho.resources;

import com.morpho.MorphoApplication;
import com.morpho.entities.Authentication;
import com.morpho.views.ViewCreator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.*;
import javax.ws.rs.core.Response;

/**
 * Created by irvin on 5/17/17.
 * Poner aquí cualquier solicitud que no pide una página entera.
 */
@Path("/methods")
@Produces({MediaType.TEXT_HTML})
public class AppResourcesMethods {
    ViewCreator viewCreator;

    public AppResourcesMethods(){
        viewCreator = new ViewCreator();
    }

    @POST
    @Path("example")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response example(@FormParam("Piece") String piece) {
        //Cómo usar esta versin de JSON en Java, vea aquí: https://www.tutorialspoint.com/json/json_java_example.htm
        //return viewCreator.getSamplePage();

        ResponseBuilder builder = Response.ok(" Mensaje de respuesta :D");
        //builder.entity(obj); si necesita retornar un objeto json en string, lo mete donde esta obj.
        builder.status(200); //200 es exitoso
        builder.type(MediaType.TEXT_HTML_TYPE);
        return builder.build();
    }

    @POST
    @Path("/createPiece")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createPiece(String receivedContent) {
        return queryDB("insert", "piece", receivedContent).build();
    }

    @POST
    @Path("/createComposition")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createComposition(String receivedContent) {
        return queryDB("insert", "composition", receivedContent).build();
    }

    @POST
    @Path("/sendToken")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendToken(String receivedAuth) {
        return authorize(receivedAuth).build();
    }

    private ResponseBuilder queryDB(String queryType, String collection, String receivedContent) {
        return queryDB(queryType, collection, receivedContent, null);
    }

    private ResponseBuilder queryDB(String queryType, String collection, String receivedContent, String filter) {
        ResponseBuilder builder;
        try {
            JSONObject authJSON = (JSONObject) new JSONParser().parse(receivedContent);
            String receivedAuth = authJSON.get("auth").toString();
            String content = authJSON.get(collection).toString();
            builder = authorize(receivedAuth);
            if(builder.build().getStatus() == 200) { //successful authorization
                try {
                    builder = Response.ok(collection + " created");
                    builder.status(200);
                    if(queryType == "insert")
                        MorphoApplication.DBA.insert(collection, content);
                    else if(queryType == "update")
                        MorphoApplication.DBA.update(collection, filter, content);
                    else if(queryType == "delete")
                        MorphoApplication.DBA.delete(collection, content);
                    else {
                        builder = Response.ok("Invalid query type");
                        builder.status(500);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    builder = Response.ok("Error inserting into DB");
                    builder.status(404);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            builder = Response.ok("Could not process auth");
            builder.status(422);
        }
        return builder;
    }

    private ResponseBuilder authorize(String receivedAuth) {
        ResponseBuilder builder;
        try {
            JSONObject authJSON = (JSONObject) new JSONParser().parse(receivedAuth);
            String userID = (String) authJSON.get("userID");
            String accessToken = (String) authJSON.get("accessToken");
            System.out.println("user ID: " + userID);
            if(MorphoApplication.Auth.verifyUser(userID, accessToken)) {
                try {
                    MorphoApplication.DBA.set("users", new Authentication(userID, accessToken).toString());
                    builder = Response.ok("Valid token sent");
                    builder.status(200);
                } catch(Exception e) {
                    builder = Response.ok("Error inserting into DB");
                    builder.status(404);
                }
            } else {
                builder = Response.ok("Unauthorized: invalid token");
                builder.status(401);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            builder = Response.ok("Could not process auth");
            builder.status(422);
        }
        return builder;
    }
}
