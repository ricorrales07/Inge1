package com.morpho.resources;

import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;
import com.morpho.MorphoApplication;
import com.morpho.entities.Authentication;
import com.morpho.views.ViewCreator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.text.Document;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.net.URLDecoder;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by irvin on 29/4/2017.
 * These are resources that can be accessed through AJAX, without the need to load a whole page.
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
        try {
            BufferedReader reader = new BufferedReader(new FileReader(".\\src\\main\\resources\\assets\\imagesData\\PieceCounter.txt"));
            this.pieceCounter = Integer.parseInt(reader.readLine());
            reader.close();
            reader = new BufferedReader(new FileReader(".\\src\\main\\resources\\assets\\imagesData\\CompositionCounter.txt"));
            this.compositionCounter = Integer.parseInt(reader.readLine());
            reader.close();
        }catch(Exception e){

        }
    }

    /*
    getImages: Using the specified path where images are stored,
               it gets all files directories with extension .png
               of said path and use them to construct a String
               that will represent html code (that, when loaded,
               will show images with an onClick property) that
               will be the builder's entity.

     returns: builder.build()
     */
    @GET
    @Path("getImages")
    public Response getImages() {
        ResponseBuilder builder;
        File directory = new File(".\\src\\main\\resources\\assets\\images");
        String html="";
        //String html= "<div class=\"funSlick\">";
        for (File file : directory.listFiles())
        {
            if(file.getName().endsWith(".png")) //Por ahora solo extensiones .png
            {
                html = html + "<a data-dismiss=\"modal\"> <img src=\"assets/images/" + file.getName() + "\" style=\"width:27%; height:27%; padding:10px; margin:10px;\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this)\" /> </a>";
            }
        }
        //html = html + "</div>";
        builder = Response.ok("Got images");
        builder.entity(html);
        builder.status(200);
        return builder.build();
    }

    //TODO: cambiar este método para que tire pocos resultados (de 10 en 10 o así)
    @GET
    @Path("getPiecesInDB")
    public Response getPiecesInDB() {
        ResponseBuilder builder;
        File directory = new File(".\\src\\main\\resources\\assets\\images");
        String html= "";
        FindIterable<org.bson.Document> imgJsons;
        try
        {
            imgJsons = MorphoApplication.DBA.search("piece", "{}");

            //TODO: sacar esto de acá, solo debería ir la línea anterior
            for (org.bson.Document json : imgJsons)
            {
                html += "<modalImage data-dismiss=\"modal\"> <img src=\""
                    + json.getString("SourceFront")
                    + "\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this, '" + json.getString("_id") + "')\" /> </a>";
            }
        }
        catch (Exception e) //DANGER
        {
            //TODO: log
            builder = Response.status(404);
            builder.entity(e.toString());
            //builder.status(200);
            return builder.build();
        }

        builder = Response.ok("Got images");
        builder.entity(html);
        builder.status(200);
        return builder.build();
    }

    @POST
    @Path("example")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response example(@FormParam("Authentication") String piece) {
        //Cómo usar esta versin de JSON en Java, vea aquí: https://www.tutorialspoint.com/json/json_java_example.htm
        //return viewCreator.getSamplePage();

        ResponseBuilder builder = Response.ok(" Mensaje de respuesta :D");
        //builder.entity(obj); si necesita retornar un objeto json en string, lo mete donde esta obj.
        builder.status(200); //200 es exitoso
        builder.type(MediaType.TEXT_HTML_TYPE);
        return builder.build();
    }

    /**
     * Add a new piece into the database.
     * @param receivedContent, this is the piece object represented in a JSON string.
     * @return server response.
     */
    @POST
    @Path("/createPiece")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response createPiece(String receivedContent) {
        return queryDB("insert", "piece", receivedContent).build();
    }

    /**
     * Creates a new composition and adds it to the database.
     * @param receivedContent is the composition object represented in a JSON string.
     * @return server response.
     */
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
                        MorphoApplication.DBA.set(collection, content);
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
            URLDecoder.decode(receivedContent, "UTF8");
            String[] data = receivedContent.split(",");

            try {
                if(data[0].equals("Piece")) {
                    String imageData = data[2];
                    String imageDataB = data[4];
                    InputStream bit = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageData));
                    InputStream bitB = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageDataB));
                    ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\PieceA" + pieceCounter + ".png"));
                    ImageIO.write(ImageIO.read(bitB), "png", new File(".\\src\\main\\resources\\assets\\images\\PieceB" + pieceCounter + ".png"));
                }else{
                    String imageData = data[2];
                    InputStream bit = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageData));
                    ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\Composition" + compositionCounter + ".png"));
                }

                builder = Response.ok("Image saved");
                builder.status(200);

                if(this.saved){
                    this.saved = false;
                    if(data[0].equals("Piece")){
                        this.pieceCounter++;
                        try {
                            PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\PieceCounter.txt");
                            writer.print(""+pieceCounter);
                            writer.close();
                        }catch(Exception e){

                        }
                    }else{
                        this.compositionCounter++;
                        try {
                            PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\CompositionCounter.txt");
                            writer.print(""+compositionCounter);
                            writer.close();
                        }catch(Exception e){

                        }
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
        ResponseBuilder builder = Response.ok("Failed to save image in server");;
        try {
            JSONObject receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            JSONObject a = (JSONObject) new JSONParser().parse(receivedJSON.get("piece").toString());
            JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
            a.put("SourceFront", "assets/images/PieceA" + pieceCounter + ".png");
            a.put("SourceSide", "assets/images/PieceB" + pieceCounter + ".png");
            a.put("_id", id.get("userID").toString() + "C" + pieceCounter);
            receivedJSON.put("piece", a);
            receivedContent = receivedJSON.toJSONString().replaceAll("\\\\","");
            System.out.println(receivedContent);
        } catch(ParseException e){
            e.printStackTrace();
        }

        //ResponseBuilder builder = queryDB("insert", "composition", receivedContent);

        receivedContent = MorphoApplication.searcher.addSearchIdToPiece(receivedContent);

        try {
            PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\Piece" + pieceCounter + ".json");
            writer.print(receivedContent);
            writer.close();
            builder = queryDB("insert", "piece", receivedContent);
        }catch(Exception e){

        }
        if(this.saved){
            this.saved = false;
            this.pieceCounter++;
            try {
                PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\PieceCounter.txt");
                writer.print(""+pieceCounter);
                writer.close();

            }catch(Exception e){

            }
        }else{
            this.saved = true;

        }
        return builder.build();
    }

    @POST
    @Path("/saveCompositionData")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveCompositionData(String receivedContent) {
        ResponseBuilder builder;// = queryDB("insert", "piece", receivedContent);
        System.out.println(receivedContent);

        try {
            JSONObject receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            JSONObject data = (JSONObject) new JSONParser().parse(receivedJSON.get("composition").toString());
            JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
            data.put("_id", id.get("userID").toString() + "C" + compositionCounter);
            receivedJSON.put("composition", data);
            receivedContent = receivedJSON.toJSONString().replaceAll("\\\\","");
            System.out.println(receivedContent);
        } catch(ParseException e){
            e.printStackTrace();
        }

        try {
            PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\Composition" + compositionCounter + ".json");
            writer.print(receivedContent);
            writer.close();


        }catch(Exception e){

        }
        if(this.saved){
            this.saved = false;
            this.compositionCounter++;
            try {
                PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\CompositionCounter.txt");
                writer.print(""+compositionCounter);
                writer.close();
            }catch(Exception e){

            }
        }else{
            this.saved = true;
        }
        receivedContent = MorphoApplication.searcher.addSearchIdToComposition(receivedContent);
        builder = queryDB("insert", "composition", receivedContent);
        return builder.build();
    }

    @POST
    @Path("trySearch")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response trySearch(String receivedContent) {
        System.out.println(receivedContent);
        try {
            JSONObject receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            JSONObject data = (JSONObject) new JSONParser().parse(receivedJSON.get("composition").toString());
            JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
            data.put("_id", id.get("userID").toString() + "C" + compositionCounter);
            receivedJSON.put("composition", data);
            //receivedContent = receivedJSON.toJSONString().replaceAll("\\\\","");
            System.out.println(receivedContent);
        } catch(ParseException e){
            e.printStackTrace();
        }
        receivedContent = MorphoApplication.searcher.addSearchIdToComposition(receivedContent);


        ArrayList<String> results =
                MorphoApplication.searcher.searchSimilarCompositions(receivedContent, 1);

        ResponseBuilder builder = Response.ok();

        String jsons = "[";

        for (String j : results) {
            System.out.println("Result: " + j);
            jsons += j + ", ";
        }

        System.out.println("Found " + results.size() + " results.");

        if (jsons.length() > 1)
            jsons = jsons.substring(0, jsons.length() - 1);
        jsons += "]";

        System.out.println("Search results: " + jsons);

        builder.entity(jsons);
        builder.status(200);
        return builder.build();
    }
}
