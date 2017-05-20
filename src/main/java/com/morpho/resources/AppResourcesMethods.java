package com.morpho.resources;

import com.mongodb.util.JSON;
import com.morpho.MorphoApplication;
import com.morpho.entities.Authentication;
import com.morpho.views.ViewCreator;
import jdk.nashorn.internal.runtime.ParserException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.text.html.parser.Parser;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.*;
import javax.ws.rs.core.Response;

import static com.morpho.MorphoApplication.DBA;

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
    @Path("savePiece")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response savePiece(@FormParam("Piece") String piece ) {
        //Cómo usar esta versin de JSON en Java, vea aquí: https://www.tutorialspoint.com/json/json_java_example.htm
        //return viewCreator.getSamplePage();

        ResponseBuilder builder = Response.ok(" Mensaje de respuesta :D");
        //builder.entity(obj); si necesita retornar un objeto json en string, lo mete donde esta obj.
        builder.status(200); //200 es exitoso
        builder.type(MediaType.TEXT_HTML_TYPE);
        return builder.build();
    }

    /*@POST
    @Path("/sendToken")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendToken(Authentication auth) {
        String userID = auth.getUserID();
        String accessToken = auth.getAccessToken();

        System.out.println("I\'m here");
        MorphoApplication.DBA.insert("users", auth.toString());
        System.out.println("into db");
        MorphoApplication.userID = userID;
        MorphoApplication.accessToken = accessToken;

        ResponseBuilder builder = Response.ok(" Token sent");
        builder.status(200);
        return builder.build();
    } //No pude hacerlo con el POJO */

    @POST
    @Path("/sendToken")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendToken(String auth) {
        System.out.println(auth);
        ResponseBuilder builder;
        try {
            DBA.insert("users", auth);
            JSONObject authJSON = (JSONObject) new JSONParser().parse(auth);
            MorphoApplication.userID = (String) authJSON.get("userID");
            MorphoApplication.accessToken = (String) authJSON.get("accessToken");

            builder = Response.ok(" Token sent");
            builder.status(200);
        } catch (ParseException e) {
            e.printStackTrace();
            builder = Response.status(400);
        }
        return builder.build();
    }
}
