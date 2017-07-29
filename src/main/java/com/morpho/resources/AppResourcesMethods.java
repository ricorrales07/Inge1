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

import org.bson.Document;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.*;
import javax.ws.rs.core.Response;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

import java.net.URLDecoder;
import java.util.*;

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
                html = html + "<modalImages data-dismiss=\"modal\"> <img src=\"assets/images/" + file.getName() + "\" style=\"width:27%; height:27%; padding:10px; margin:10px;\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this)\" /> </modalImages>";
            }
        }
        FindIterable<org.bson.Document> imgJsons;
        try
        {
            imgJsons = MorphoApplication.DBA.search("piece", "{}");

            //TODO: sacar esto de acá, solo debería ir la línea anterior
            for (org.bson.Document json : imgJsons)
            {
                html += "<modalImages data-dismiss=\"modal\"> <img src=\""
                        + json.getString("SourceFront")
                        + "\" class = \"img-thumbnail\" onclick=\"addImageToCanvas(this, '" + json.getString("SourceFront") + "','" + json.getString("SourceSide") + "','" + json.getString("_id") + "')\" /> </modalImages>";
            }
        }catch(Exception e){
            MorphoApplication.logger.warning(e.toString());
            builder = Response.status(404);
            builder.entity(e.toString());
            return builder.build();
        }
        builder = Response.ok("Got images");
        builder.entity(html);
        builder.status(200);
        return builder.build();
    }

    @POST
    @Path("getImagesDataInDB")
    public Response getImagesDataInDB(String receivedContent) {
        ResponseBuilder builder;
        String html= "";
        FindIterable<org.bson.Document> imgJsons;
        try
        {
            JSONObject receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            imgJsons = MorphoApplication.DBA.search(receivedJSON.get("collection").toString(), receivedJSON.get("filter").toString());

            //TODO: sacar esto de acá, solo debería ir la línea anterior
            for (org.bson.Document json : imgJsons)
            {
                String src;
                String method;
                if(receivedJSON.get("collection").toString().equals("piece")){
                    src = json.getString("SourceFront");
                    method = "addImageToCanvas(this,'" + json.getString("SourceFront") + "','" + json.getString("SourceSide") + "','" + json.getString("_id") + "')";
                }else{
                    src = json.getString("imgSource").substring(20);
                    method = "loadComposition('" + json.getString("_id") + "')";
                }
                html += "<modalImages data-dismiss=\"modal\"> <img src=\"" + src
                        + "\" class = \"img-thumbnail\" onclick=\"" + method + "\" /> </modalImages>";
            }
        }
        catch (Exception e) //DANGER
        {
            MorphoApplication.logger.warning(e.toString());
            builder = Response.status(404);
            builder.entity(e.toString());
            return builder.build();
        }

        builder = Response.ok("Got images");
        builder.entity(html);
        builder.status(200);
        return builder.build();
    }

    @POST
    @Path("/getPieceData")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getPieceData(String id){
        ResponseBuilder builder;
        Document imgJson;
        try
        {
            imgJson = MorphoApplication.DBA.search("piece", "{_id: \"" + id + "\"}").first();
        }
        catch (Exception e) //DANGER
        {
            MorphoApplication.logger.warning(e.toString());
            builder = Response.status(404);
            builder.entity(e.toString());
            return builder.build();
        }

        builder = Response.ok("Got piece data");
        builder.entity(imgJson.toJson());
        builder.status(200);
        return builder.build();
    }

    @POST
    @Path("/sendToken")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendToken(String receivedAuth) {
        ResponseBuilder b = Response.ok();
        b.status(200);
        return b.build();
    }

    @POST
    @Path("/getCompositionPieces")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response getCompositionPieces(String id) {
        FindIterable<org.bson.Document> imgJsons;
        ResponseBuilder builder = Response.ok();
        try {
            imgJsons = MorphoApplication.DBA.search("composition", "{ _id: \"" + id + "\" }");
            Document json = imgJsons.first();
            JSONObject docData = (JSONObject) new JSONParser().parse(json.toJson());
            JSONArray documentArray = (JSONArray) docData.get("pieces");
            builder.entity(documentArray.toString());

        }catch(Exception e){
            MorphoApplication.logger.warning(e.toString());
            builder = Response.status(404);
            builder.entity(e.toString());
            return builder.build();
        }

        return builder.build();
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
            builder.status(200);
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
            URLDecoder.decode(receivedContent, "UTF8");
            MorphoApplication.logger.info("recievedContent (saveCreatedImageFile): " + receivedContent);
            String[] data = receivedContent.split(",");
            String imgSource = "";
            File f = null;
            if(data[0].equals("Piece")) {
                f = new File(".\\src\\main\\resources\\assets\\images\\" + data[5]);
                MorphoApplication.logger.info("filename: " + f.getName());
                f.mkdir();
                String imageData = data[2];
                String imageDataB = data[4];
                InputStream bit = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageData));
                InputStream bitB = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageDataB));
                String fileID;
                if(data[6].equals("undefined")){
                    fileID = "" + pieceCounter;
                }else{
                    fileID = data[6].split("C")[1];
                }
                ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\" + data[5] + "\\PieceA" + fileID + ".png"));
                ImageIO.write(ImageIO.read(bitB), "png", new File(".\\src\\main\\resources\\assets\\images\\" + data[5] + "\\PieceB" + fileID + ".png"));
                builder = Response.ok("Image saved");
            }else{
                String imageData = data[2];
                InputStream bit = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(imageData));
                ImageIO.write(ImageIO.read(bit), "png", new File(".\\src\\main\\resources\\assets\\images\\" + data[3] + "\\Composition" + compositionCounter + ".png"));
                imgSource = "/resources/assets/images/" + data[3] + "/Composition/" + compositionCounter + ".png";
                builder = Response.ok("Image saved");
                builder.entity(imgSource);
            }
            builder.status(200);

            if(data[0].equals("Piece") && data[6].equals("undefined")){
                this.pieceCounter++;
                try {
                    PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\PieceCounter.txt");
                    writer.print(""+pieceCounter);
                    writer.close();
                }catch(Exception e){
                    MorphoApplication.logger.warning(e.toString());
                    e.printStackTrace();
                }
            }else if (data[0].equals("Composition")){
                this.compositionCounter++;
                try {
                    PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\CompositionCounter.txt");
                    writer.print(""+compositionCounter);
                    writer.close();
                }catch(Exception e){
                    MorphoApplication.logger.warning(e.toString());
                    e.printStackTrace();
                }
            }

            return builder.build();

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
        String currentFile = "";
        try {
            receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            currentFile = receivedJSON.get("file").toString();
            receivedJSON.remove("file");

            FindIterable<org.bson.Document> imgJsons;
            imgJsons = MorphoApplication.DBA.search("piece", "{}");

            int equalAttributes;

            JSONObject parts = (JSONObject) receivedJSON.get("piece");
            JSONObject optionalInPart = (JSONObject) parts.get("optional");

            for (org.bson.Document json : imgJsons)
            {
                if((json.size() - 4) == parts.size()) {
                    equalAttributes = 0;

                    if(parts.get("Scientific Name").toString().equalsIgnoreCase(json.get("Scientific Name").toString())
                            && parts.get("Type").toString().equalsIgnoreCase(json.get("Type").toString())){
                        Set<Map.Entry<String, Object>> currentPiece = optionalInPart.entrySet();
                        for (Map.Entry<String, Object> newKey : currentPiece) {
                            Document docData = (Document) json.get("optional");
                            Set<Map.Entry<String, Object>> currentDocument = docData.entrySet();
                            for (Map.Entry<String, Object> key : currentDocument) {
                                if (key.equals(newKey)) {
                                    equalAttributes++;
                                    break;
                                }
                            }
                        }
                        if (equalAttributes == optionalInPart.size()) {
                            equals = true;
                            break;
                        }
                    }
                }
            }
        }catch (ParseException e){
            MorphoApplication.logger.warning(e.toString());
            e.printStackTrace();
        }catch (Exception e){
            MorphoApplication.logger.warning(e.toString());
            e.printStackTrace();
        }

        if(equals){
            System.err.println("Repeated piece");
            builder = Response.ok("Repeated");
        }else {
            JSONObject a = null;
            try {
                String fileID;
                if(currentFile.equals("undefined")){
                    fileID = "" + pieceCounter;
                }else{
                    fileID = currentFile.split("C")[1];
                }
                a = (JSONObject) new JSONParser().parse(receivedJSON.get("piece").toString());
                JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
                a.put("SourceFront", "assets/images/" + id.get("userID") + "/PieceA" + fileID + ".png");
                MorphoApplication.logger.info("Saving piece: " + "assets/images/PieceA" + fileID + ".png");
                a.put("SourceSide", "assets/images/" + id.get("userID") + "/PieceB" + fileID + ".png");
                try {
                    MorphoApplication.logger.info("Saving piece: user ID: " + id.get("userID").toString());
                    a.put("_id", id.get("userID").toString() + "C" + fileID);
                } catch (NullPointerException e) {
                    a.put("_id", 0 + "C" + fileID);
                }
                receivedJSON.put("piece", a);
                receivedContent = receivedJSON.toJSONString().replaceAll("\\\\", "");
                MorphoApplication.logger.info(receivedContent);
            } catch (ParseException e) {
                MorphoApplication.logger.warning(e.toString());
                e.printStackTrace();
            } catch (Exception e) {
                MorphoApplication.logger.warning(e.toString());
                e.printStackTrace();
            }

            receivedContent = MorphoApplication.searcher.addSearchIdToPiece(receivedContent);
            MorphoApplication.logger.info("Piece with searchId: " + receivedContent);

            try {
                PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\P" + a.get("_id") + ".json");
                writer.print(receivedContent);
                writer.close();
                if(currentFile.equals("undefined")){
                    builder = queryDB("insert", "piece", receivedContent);
                }else{
                    builder = queryDB("update", "piece", receivedContent, "{ _id: \"" + currentFile + "\"}");
                }

            } catch (Exception e) {
                MorphoApplication.logger.warning("Error while inserting piece in DB: " + e.toString());
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
            MorphoApplication.logger.warning(e.toString());
            e.printStackTrace();
        }catch(Exception e){
            MorphoApplication.logger.warning(e.toString());
            e.printStackTrace();
        }

        if(equals){
            System.err.println("Repeated composition");
            builder = Response.ok("Repeated");
        }else {
            try {
                JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
                try {
                    data.put("_id", id.get("userID").toString() + "C" + compositionCounter);
                    data.put("imgSource", "./src/main/resources/assets/images/" + id.get("userID").toString() + "/Composition" + compositionCounter + ".png");
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
                PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\C" + data.get("_id") + ".json");
                writer.print(receivedContent);
                writer.close();
            } catch (Exception e) {
                MorphoApplication.logger.warning(e.toString());
            }

            try {
                PrintWriter writer = new PrintWriter(".\\src\\main\\resources\\assets\\imagesData\\CompositionCounter.txt");
                writer.print("" + compositionCounter);
                writer.close();
            } catch (Exception e) {
                MorphoApplication.logger.warning(e.toString());
            }
            receivedContent = MorphoApplication.searcher.addSearchIdToComposition(receivedContent);
            MorphoApplication.logger.info(receivedContent);
            builder = queryDB("insert", "composition", receivedContent);
        }
        return builder.build();
    }

    /*@POST
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
    }*/

    @POST
    @Path("loadPhotos")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response loadPhotos(String receivedContent) {
        ResponseBuilder builder;
        String html= "";
        FindIterable<org.bson.Document> imgJsons;
        try
        {
            String filter = "{_id: \"" + receivedContent + "C" + (compositionCounter - 1 ) + "\"}";
            imgJsons = MorphoApplication.DBA.search("composition", filter);

            //TODO: sacar esto de acá, solo debería ir la línea anterior
            for (org.bson.Document json : imgJsons)
            {
                JSONObject docData = (JSONObject) new JSONParser().parse(json.toJson());
                JSONArray documentArray = (JSONArray) docData.get("images");

                for(int i = 0; i < documentArray.size(); i++){
                    JSONObject o = (JSONObject) new JSONParser().parse(documentArray.get(i).toString());

                    html += "<modalImages data-dismiss=\"modal\"> <img src=\"" + o.get("image")
                            + "\" class = \"img-thumbnail\" /> </modalImages>";
                }
            }
        }
        catch (Exception e)
        {
            MorphoApplication.logger.warning(e.toString());
            builder = Response.status(404);
            builder.entity(e.toString());
            return builder.build();
        }

        builder = Response.ok("Got images");
        builder.entity(html);
        builder.status(200);
        return builder.build();
    }

    @GET
    @Path("getCompositionData")
    public Response getCompositionData(@QueryParam("id") String id) {
        ResponseBuilder builder;
        Document json = new Document();

        MorphoApplication.logger.info("Recieved id: " + id);

        try {
            json = MorphoApplication.DBA.documentFind("composition", "{_id: \"" + id + "\"}");
        }
        catch (Exception e) {
            MorphoApplication.logger.warning("Error when searching for composition: " + e.toString());
        }

        Document attributes = json.get("attributes", Document.class);

        String html = "<h1>" + attributes.getString("Scientific Name") + "</h1><br>";

        for(String key : attributes.keySet()) {
            if (!key.equals("Scientific Name"))
                html += key + ": " + json.get(key).toString() + "<br>";
        }

        MorphoApplication.logger.info("html result: " + html);

        builder = Response.ok("Successfuly fetched data");
        builder.entity(html);
        builder.status(200);
        return builder.build();
    }
}
