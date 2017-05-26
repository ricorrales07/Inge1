package com.morpho.resources;

import com.morpho.MorphoApplication;
import com.morpho.entities.Authentication;
import com.morpho.views.ViewCreator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.Base64.Decoder;

/**
 * Created by irvin on 5/17/17.
 * Poner aquí cualquier solicitud que no pide una página entera.
 */
@Path("/methods")
@Produces({MediaType.TEXT_HTML})
public class AppResourcesMethods {
    ViewCreator viewCreator;
    int pieceCounter = 0;
    int compositionCounter = 0;
    boolean saved = false;

    public AppResourcesMethods(){
        viewCreator = new ViewCreator();
    }

    @GET
    @Path("getImages")
    public Response getImages() {
        ResponseBuilder builder;
        File directory = new File(".\\src\\main\\resources\\assets\\images");
        String html="";
        for (File file : directory.listFiles())
        {
            if(file.getName().endsWith(".png")) //Por ahora solo extensiones .png
            {
                html = html + "<a href=\"#close\"> <img src=\"assets/images/" + file.getName() + "\" onclick=\"enCanvas(this)\" /> </a>";
            }
        }
        builder = Response.ok("Got images");
        builder.entity(html);
        builder.status(200);
        return builder.build();
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

    @POST
    @Path("/saveCreatedImageFile")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveCreatedImageFile(String receivedContent){
        ResponseBuilder builder;
        try {
            System.out.println(receivedContent);
            URLDecoder.decode(receivedContent, "UTF8");
            String type = receivedContent.split(",")[0].split(":")[1].replaceAll("\"", "");

            try {
                if(type.replaceAll("}","").equals("Piece")) {
                    String imageData = receivedContent.split(",")[2];
                    String imageDataB = receivedContent.split(",")[4];
                    byte[] real = DatatypeConverter.parseBase64Binary(imageData);
                    byte[] realB = DatatypeConverter.parseBase64Binary(imageDataB);
                    InputStream bit = new ByteArrayInputStream(real);
                    InputStream bitB = new ByteArrayInputStream(realB);
                    ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\PieceA" + pieceCounter + ".png"));
                    ImageIO.write(ImageIO.read(bitB), "png", new File(".\\src\\main\\resources\\assets\\images\\PieceB" + pieceCounter + ".png"));
                }else{
                    String imageData = receivedContent.split(",")[2];
                    byte[] real = DatatypeConverter.parseBase64Binary(imageData);
                    InputStream bit = new ByteArrayInputStream(real);
                    ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\Composition" + compositionCounter + ".png"));
                }

                builder = Response.ok("Image saved");
                builder.status(200);

                if(this.saved){
                    this.saved = false;
                    if(type.replaceAll("}","").equals("Piece")){
                        this.pieceCounter++;
                    }else{
                        this.compositionCounter++;
                    }
                }else{
                    this.saved = true;
                }

                return builder.build();
            } catch (Exception e) {
                System.err.println("Failed to save image in server");
                builder = Response.ok("Failed to save image in server");
                builder.status(404);
                return builder.build();
            }
        }catch (Exception e){
            System.err.println("Failed to save image in server");
            builder = Response.ok("Failed to save image in server");
            builder.status(404);
            return builder.build();
        }
    }

    @POST
    @Path("/saveAttributes")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveAttributes(String receivedContent){
        ResponseBuilder builder;
        try {
            PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\images\\Piece" + pieceCounter + ".json");
            writer.print(receivedContent + "\"SourceFront\": \"assets/images/PieceA" + pieceCounter + ".png\",\n \"SourceFront\": \"assets/images/PieceB" + pieceCounter + ".png\"\n}");
            writer.close();
        }catch(Exception e){

        }
        if(this.saved){
            this.saved = false;
            this.pieceCounter++;
        }else{
            this.saved = true;
        }
        builder = Response.ok("Attribute saved");
        return builder.build();
    }

    @POST
    @Path("/saveCompositionData")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveCompositionData(String receivedContent) {
        ResponseBuilder builder;
        System.out.println(receivedContent);
        try {
            PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\images\\Composition" + compositionCounter + ".json");
            writer.print(receivedContent);
            writer.close();
        }catch(Exception e){

        }
        if(this.saved){
            this.saved = false;
            this.compositionCounter++;
        }else{
            this.saved = true;
        }
        builder = Response.ok("Attribute saved");
        return builder.build();
    }
}
