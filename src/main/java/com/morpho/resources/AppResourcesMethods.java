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
import java.util.*;
import java.util.Base64.Decoder;

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
            MorphoApplication.logger.warning(e.toString());
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
        for (File file : directory.listFiles())
        {
            if(file.getName().endsWith(".png") || file.getName().endsWith(".PNG")) //Por ahora solo extensiones .png
            {
                //html = html + "<a data-dismiss=\"modal\"> <img src=\"assets/images/" + file.getName() + "\" style=\"width:27%; height:27%; padding:10px; margin:10px;\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this)\" /> </a>";
                html = html + "<modalImages data-dismiss=\"modal\"> <img src=\"assets/images/" + file.getName() + "\" onmouseover=\"this.width='auto'; this.height='auto';\" onmouseout=\"this.width='150'; this.height='150';\" style=\"width:150px; height:150px; padding:10px; margin:10px;\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this)\" /> </modalImages>";

            }
        }
        builder = Response.ok("Got images");
        builder.entity(html);
        builder.status(200);
        return builder.build();
    }

    @POST
    @Path("getOwnedImages")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getOwnedImages(String receivedContent) {
        ResponseBuilder builder;
        try {
            //JSONObject infoJSON = (JSONObject) new JSONParser().parse(receivedContent);
            //String imgOrn = (String) infoJSON.get("imgOrn");
            File directory = new File(".\\src\\main\\resources\\assets\\images\\" + receivedContent);
            //directory.mkdir();
            //MorphoApplication.logger.info(receivedContent);
            String html="";
            for (File file : directory.listFiles())
            {
                if(file.getName().endsWith(".png") || file.getName().endsWith(".PNG")) //Por ahora solo extensiones .png
                {
                    html = html + "<modalImages data-dismiss=\"modal\"> <img src=\"assets/images/" + receivedContent + "/" + file.getName() + "\" style=\"width:27%; height:27%; padding:10px; margin:10px;\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this)\" /> </modalImages>";
                    //html = html + "<a data-dismiss=\"modal\"> <img src=\"assets/images/" + file.getName() + "\" onmouseover=\"this.width='auto'; this.height='auto';\" onmouseout=\"this.width='150'; this.height='150';\" style=\"width:150px; height:150px; padding:10px; margin:10px;\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this)\" /> </a>";

                }
            }
            builder = Response.ok("Got images");
            builder.entity(html);
            builder.status(200);
            return builder.build();
        } catch(Exception e){
            MorphoApplication.logger.warning("Failed to save image in server");
            MorphoApplication.logger.warning(e.toString());
            builder = Response.ok("Failed to save image in server");
            builder.status(404);
            return builder.build();
        }

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
            MorphoApplication.logger.warning(e.toString());
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
    @Path("/sendToken")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendToken(String receivedAuth) {
        ResponseBuilder b = Response.ok();
        b.status(200);
        return b.build();//authorize(receivedAuth).build();
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
            builder = Response.ok();
            builder.status(200);//authorize(receivedAuth);
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
                    MorphoApplication.logger.warning("Error when querying DB: " + e.toString());
                    e.printStackTrace();
                    builder = Response.ok("Error inserting into DB");
                    builder.status(404);
                }
            }
        } catch (ParseException e) {
            MorphoApplication.logger.warning("Could not process auth: " + e.toString());
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
            MorphoApplication.logger.warning(e.toString());
            e.printStackTrace();
            builder = Response.ok("Could not process auth");
            builder.status(422);
        }
        return builder;
    }

    /**
     * Add a new piece into the database.
     * @param receivedContent, this is the piece object represented in a JSON string.
     * @return server response.
     */
    @POST
    @Path("/saveCreatedImageFile")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveCreatedImageFile(String receivedContent){
        ResponseBuilder builder;
        try {
            //JSONObject infoJSON = (JSONObject) new JSONParser().parse(receivedContent);
            //String imgOrn = (String) infoJSON.get("imgOrn");
            //String userID = (String) infoJSON.get("userID");
            URLDecoder.decode(receivedContent, "UTF8");
            String[] data = receivedContent.split(",");
            String imgSource = "";
            File f = null;
            try {
                if(data[0].equals("Piece")) {
                    f = new File(".\\src\\main\\resources\\assets\\images\\" + data[5]);
                    f.mkdir();
                    String imageData = data[2];
                    String imageDataB = data[4];
                    InputStream bit = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageData));
                    InputStream bitB = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageDataB));
                    ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\" + data[5] + "\\PieceA" + pieceCounter + ".png"));
                    ImageIO.write(ImageIO.read(bitB), "png", new File(".\\src\\main\\resources\\assets\\images\\" + data[5] + "\\PieceB" + pieceCounter + ".png"));
                    builder = Response.ok("Image saved");
                }else{
                    String imageData = data[2];
                    InputStream bit = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageData));
                    ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\" + data[3] + "\\Composition" + compositionCounter + ".png"));
                    imgSource = "./src/main/resources/assets/images/Composition/" + data[3] + "/" + compositionCounter + ".png";
                    builder = Response.ok("Image saved");
                    builder.entity(imgSource);
                }

                //builder = Response.ok("Image saved");
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
                MorphoApplication.logger.warning("Failed to save image in server");
                MorphoApplication.logger.warning(e.toString());
                builder = Response.ok("Failed to save image in server");
                builder.status(404);
                return builder.build();
            }

        }catch (Exception e){
            MorphoApplication.logger.warning("Failed to save image in server");
            MorphoApplication.logger.warning(e.toString());
            builder = Response.ok("Failed to save image in server");
            builder.status(404);
            return builder.build();
        }

    }

    /**
     * Creates a new composition and adds it to the database.
     * @param receivedContent is the composition object represented in a JSON string.
     * @return server response.
     */
    @POST
    @Path("/saveAttributes")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveAttributes(String receivedContent){
        ResponseBuilder builder = Response.ok("Failed to save image in server");;
        JSONObject receivedJSON = null;
        Boolean equals = false;
        try {
            receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);

            FindIterable<org.bson.Document> imgJsons;
            imgJsons = MorphoApplication.DBA.search("piece", "{}");

            int equalAttributes;

            JSONObject parts = (JSONObject) receivedJSON.get("piece");

            for (org.bson.Document json : imgJsons)
            {
                if((json.size() - 4) == parts.size()) {
                    equalAttributes = 0;
                    Set<Map.Entry<String, Object>> currentPiece = parts.entrySet();
                    for (Map.Entry<String, Object> newKey : currentPiece) {
                        Set<Map.Entry<String, Object>> currentDocument = json.entrySet();
                        for (Map.Entry<String, Object> key : currentDocument) {
                            if (!key.getKey().equalsIgnoreCase("_id") &&
                                    !key.getKey().equalsIgnoreCase("SourceFront") &&
                                    !key.getKey().equalsIgnoreCase("SourceSide") &&
                                    !key.getKey().equalsIgnoreCase("searchId")) {
                                if (key.equals(newKey)) {
                                    equalAttributes++;
                                    break;
                                }
                            }
                        }
                    }
                    if (equalAttributes == parts.size()) {
                        equals = true;
                        break;
                    }
                }
            }
        }catch (ParseException e){

        }catch (Exception e){

        }

        if(equals){
            System.err.println("Repeated piece");
            builder = Response.ok("Repeated");
        }else {
            try {
                JSONObject a = (JSONObject) new JSONParser().parse(receivedJSON.get("piece").toString());
                JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
                a.put("SourceFront", "assets/images/PieceA" + pieceCounter + ".png");
                MorphoApplication.logger.info("Saving piece: " + "assets/images/PieceA" + pieceCounter + ".png");
                a.put("SourceSide", "assets/images/PieceB" + pieceCounter + ".png");
                try {
                    MorphoApplication.logger.info("Saving piece: user ID: " + id.get("userID").toString());
                    a.put("_id", id.get("userID").toString() + "C" + pieceCounter);
                } catch (NullPointerException e) {
                    a.put("_id", 0 + "C" + pieceCounter);
                }
                receivedJSON.put("piece", a);
                receivedContent = receivedJSON.toJSONString().replaceAll("\\\\", "");
                MorphoApplication.logger.info(receivedContent);
            } catch (ParseException e) {
                MorphoApplication.logger.warning(e.toString());
                e.printStackTrace();
            } catch (Exception e) {

            }

            receivedContent = MorphoApplication.searcher.addSearchIdToPiece(receivedContent);
            MorphoApplication.logger.info("Piece with searchId: " + receivedContent);

            try {
                PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\Piece" + pieceCounter + ".json");
                writer.print(receivedContent);
                writer.close();
                builder = queryDB("insert", "piece", receivedContent);
            } catch (Exception e) {
                MorphoApplication.logger.warning("Error while inserting piece in DB: " + e.toString());
            }
            if (this.saved) {
                this.saved = false;
                this.pieceCounter++;
                try {
                    PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\PieceCounter.txt");
                    writer.print("" + pieceCounter);
                    writer.close();

                } catch (Exception e) {
                    MorphoApplication.logger.warning(e.toString());
                }
            } else {
                this.saved = true;

            }
        }
        return builder.build();
    }

    @POST
    @Path("/saveCompositionData")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response saveCompositionData(String receivedContent) {
        ResponseBuilder builder;
        MorphoApplication.logger.info(receivedContent);
        JSONObject receivedJSON = null;
        Boolean equals = false;
        JSONObject data = null;
        try{
            receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            data = (JSONObject) new JSONParser().parse(receivedJSON.get("composition").toString());

            FindIterable<org.bson.Document> imgJsons;
            imgJsons = MorphoApplication.DBA.search("composition", "{}");

            int equalAttributes;

            JSONObject parts = (JSONObject) receivedJSON.get("composition");
            JSONArray partsArray = (JSONArray) parts.get("pieces");

            for (org.bson.Document json : imgJsons)
            {
                JSONObject docData = (JSONObject) new JSONParser().parse(json.toJson());
                equalAttributes = 0;

                JSONArray documentArray = (JSONArray) docData.get("pieces");

                if(documentArray.size() == partsArray.size()) {

                    for (int i = 0; i < partsArray.size(); i++) {
                        JSONObject o1 = (JSONObject) new JSONParser().parse(partsArray.get(i).toString());
                        for (int j = 0; j < documentArray.size(); j++) {
                            JSONObject o2 = (JSONObject) new JSONParser().parse(documentArray.get(j).toString());
                            if (o1.get("Source1").equals(o2.get("Source1"))) {
                                if (o1.get("Source2").equals(o2.get("Source2"))) {
                                    equalAttributes++;
                                    break;
                                }
                            }
                        }
                    }
                    if (equalAttributes == partsArray.size()) {
                        equals = true;
                        break;
                    }
                }
            }
        }catch(ParseException e){

        }catch(Exception e){

        }

        if(equals){
            System.err.println("Repeated piece");
            builder = Response.ok("Repeated");
        }else {
            try {
                JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
                try {
                    data.put("_id", id.get("userID").toString() + "C" + compositionCounter);
                } catch (NullPointerException e) {
                    data.put("_id", "0C" + compositionCounter);
                }
                receivedJSON.put("composition", data);
                receivedContent = receivedJSON.toJSONString().replaceAll("\\\\", "");
                MorphoApplication.logger.info(receivedContent);
            } catch (ParseException e) {
                MorphoApplication.logger.warning(e.toString());
                e.printStackTrace();
            }

            try {
                PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\Composition" + compositionCounter + ".json");
                writer.print(receivedContent);
                writer.close();
            } catch (Exception e) {
                MorphoApplication.logger.warning(e.toString());
            }
            if (this.saved) {
                this.saved = false;
                this.compositionCounter++;
                try {
                    PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\CompositionCounter.txt");
                    writer.print("" + compositionCounter);
                    writer.close();
                } catch (Exception e) {
                    MorphoApplication.logger.warning(e.toString());
                }
            } else {
                this.saved = true;
            }
            receivedContent = MorphoApplication.searcher.addSearchIdToComposition(receivedContent);
            MorphoApplication.logger.info(receivedContent);
            builder = queryDB("insert", "composition", receivedContent);
        }
        return builder.build();
    }

    @POST
    @Path("trySearch")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response trySearch(String receivedContent) {
        MorphoApplication.logger.info(receivedContent);
        try {
            JSONObject receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            JSONObject data = (JSONObject) new JSONParser().parse(receivedJSON.get("composition").toString());
            JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
            try {
                data.put("_id", id.get("userID").toString() + "C" + compositionCounter);
            }
            catch (NullPointerException e)
            {
                data.put("_id", "0C" + compositionCounter);
            }
            receivedJSON.put("composition", data);
            //receivedContent = receivedJSON.toJSONString().replaceAll("\\\\","");
            MorphoApplication.logger.info("Received content: " + receivedContent);
        } catch(ParseException e){
            MorphoApplication.logger.warning(e.toString());
            e.printStackTrace();
        }
        receivedContent = MorphoApplication.searcher.addSearchIdToComposition(receivedContent);


        ArrayList<String> results =
                MorphoApplication.searcher.searchSimilarCompositions(receivedContent, 1);

        ResponseBuilder builder = Response.ok();

        String jsons = "[";

        for (String j : results) {
            MorphoApplication.logger.info("Result: " + j);
            jsons += j + ", ";
        }

        MorphoApplication.logger.info("Found " + results.size() + " results.");

        if (jsons.length() > 1)
            jsons = jsons.substring(0, jsons.length() - 2);
        jsons += "]";

        MorphoApplication.logger.info("Search results: " + jsons);

        builder.entity(jsons);
        builder.status(200);
        return builder.build();
    }


}
