package com.morpho.views;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import com.mongodb.client.FindIterable;
import com.morpho.MorphoApplication;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sun.awt.ModalExclude;

import org.bson.Document;

//import javax.swing.text.Document;
import javax.validation.constraints.Max;
import javax.xml.bind.DatatypeConverter;

/**
 * Created by Irvin Umaña on 29/4/2017.
 * This class contains different methods that build the different templates (views)
 * These methods are usually called inside a resource request to build a page, or any other HTML.
 */
public class ViewCreator {
    final StringTemplateGroup group = new StringTemplateGroup("templates","./templates");

    public ViewCreator(){

    }

    private String getProfile(String name, String picture, String userId)
    {
        StringTemplate profileTemplate = group.getInstanceOf("profile");
        profileTemplate.setAttribute("loginModal", group.getInstanceOf("loginModal"));

        String userInfo = "", email="", institution="", phone="";
        try {
            userInfo = MorphoApplication.DBA.find("users", "{_id: \"" + userId + "\"}");
            JSONObject dataJSON = (JSONObject) new JSONParser().parse(userInfo);
            institution = (String) dataJSON.get("institution");
            phone = (String) dataJSON.get("phone");
            email = (String) dataJSON.get("email");
            MorphoApplication.logger.info("Found user: " + userId
                    + " with institution: " + institution);
        } catch (Exception e) {
            MorphoApplication.logger.warning(e.toString());
            institution = phone = email = "ERROR";
        }

        FindIterable<Document> pieceList = null;
        String pieces = "";
        try {
            MorphoApplication.logger.info("Searching for pieces of user with id: "
                    + userId);
            pieceList = MorphoApplication.DBA.search("piece",
                    "{ownerId: \"" + userId + "\"}");
            for(Document piece : pieceList)
            {
                MorphoApplication.logger.info("Adding piece: "
                        + piece.getString("SourceFront"));

                //TODO: obtener votos reales
                pieces += "<div class=\"item\">\n" +
                        "<img src=\"data:image/png;base64," + MorphoApplication.getImageBytes(piece.getString("SourceFront")) + "\" style=\"border-style:solid;border-width:1px;border-color:black;\">\n" +
                        "<div class=\"glyphicon glyphicon-menu-up\" align=\"right\" style=\"color:#00a0da\">" + "0" + "</div>\n" +
                        "<div class=\"glyphicon glyphicon-menu-down\" align=\"right\" style=\"color:#00a0da\">" + "0" + "</div>" +
                        "</div>";
            }
            if (pieces.equals(""))
                pieces = "No pieces found for this user.";
        } catch (Exception e)
        {
            MorphoApplication.logger.warning(e.toString());
            pieces = "No pieces found for this user.";
        }

        FindIterable<Document> compositionList = null;
        String compositions = "";
        try {
            MorphoApplication.logger.info("Searching for compositions of user with id: "
                    + userId);
            compositionList = MorphoApplication.DBA.search("composition",
                    "{\"attributes.ownerId\": \"" + userId + "\"}");
            for(Document composition : compositionList)
            {
                MorphoApplication.logger.info("Adding composition: "
                        + composition.getString("imgSource"));

                compositions += "<div class=\"item\">\n" +
                        "<img src=\"data:image/png;base64," +
                        MorphoApplication.getImageBytes(composition.getString("imgSource")) +
                        "\" style=\"border-style:solid;border-width:1px;border-color:black;\">\n" +
                        "<div class=\"glyphicon glyphicon-menu-up\" align=\"right\" style=\"color:#00a0da\">" + "0" + "</div>\n" +
                        "<div class=\"glyphicon glyphicon-menu-down\" align=\"right\" style=\"color:#00a0da\">" + "0" + "</div>" +
                        "</div>";
            }
            if (compositions.equals(""))
                compositions = "No compositions found for this user.";
        } catch (Exception e)
        {
            MorphoApplication.logger.warning(e.toString());
            compositions = "No compositions found for this user.";
        }

        profileTemplate.setAttribute("pieces", pieces);
        profileTemplate.setAttribute("compositions", compositions);
        profileTemplate.setAttribute("picture", "\"" + picture + "\"");
        profileTemplate.setAttribute("name", name);
        profileTemplate.setAttribute("institution", institution);
        profileTemplate.setAttribute("phone", phone);
        profileTemplate.setAttribute("email", email);

        return profileTemplate.toString();
    }

    public String getProfileUsingGmail(String accessToken){
        String response = MorphoApplication.Auth.getResponseFromURL("https://www.googleapis.com/oauth2/v3/tokeninfo?id_token="
                + accessToken);

        MorphoApplication.logger.info("JSON gotten when opening profile: " + response);

        String name = "Name error", picture = "", userId = "";
        try {
            JSONObject dataJSON = (JSONObject) new JSONParser().parse(response);
            name = (String) dataJSON.get("name");
            picture = (String) dataJSON.get("picture") + "?sz=200";
            userId = (String) dataJSON.get("sub");
        } catch(ParseException e) {
            MorphoApplication.logger.warning(e.toString());
        }

        return getProfile(name, picture, userId);
    }

    public String getProfileUsingFacebook(String accessToken, String userId){
        String response = MorphoApplication.Auth.getResponseFromURL("https://graph.facebook.com/"
                + userId + "?access_token=" + accessToken
                + "&fields=name,picture.height(200)");

        MorphoApplication.logger.info("JSON gotten when opening profile: " + response);

        String name = "Name error", picture = "";
        try {
            JSONObject dataJSON = (JSONObject) new JSONParser().parse(response);
            name = (String) dataJSON.get("name");
            picture = (String) ((JSONObject) ((JSONObject) dataJSON.get("picture")).get("data")).get("url");
        } catch(ParseException e) {
            MorphoApplication.logger.warning(e.toString());
        }

        return getProfile(name, picture, userId);
    }

    /**
     * Generates the html needed for the createPiecePage.
     * @return HTML of the page in a string object.
     */
    public String getCreatePiecePage(){
        StringTemplate templateSample = group.getInstanceOf("createPiece");
        StringTemplate pieceSearchResults = group.getInstanceOf("pieceSearchResults");
        templateSample.setAttribute("PIECE_SEARCH_RESULTS", pieceSearchResults);
        templateSample.setAttribute("loginModal",group.getInstanceOf("loginModal"));
        return templateSample.toString();
    }

    /**
     * Generates the html needed for the editPiecePage.
     * @param pieceId identifies the piece that needs to be edited.
     * @return HTML of the page in a string object.
     * STATUS: Still not finished completely.
     */
    public String getEditPiecePage(String pieceId){
        // Con pieceId voy a la base de datos para obtener los datos.
        StringTemplate templateSample = group.getInstanceOf("createPiece"); //USA EL MISMO TEMPLATE
        //templateSample.setAttribute("ESTUDIANTE","lol");  //Así como este atributo se puede meter las imagenes y
        //las propiedades de la pieza en la página. Tal vez en un div que tenga display:none. Despues con javascript
        //selecciono este div, le saco los datos que quiero, and "populate" las diferentes tarjetas y las vistas (surfaces en easel).
        templateSample.setAttribute("loginModal",group.getInstanceOf("loginModal"));
        return templateSample.toString();
    }

    /**
     * Generate the html needed for the home page.
     * @return HTML of the page in a string object.
     */
    public String getHomepage()
    {
        StringTemplate templateHomepage = group.getInstanceOf("homepage" );
        StringTemplate similaritySearchResults = group.getInstanceOf("similaritySearchResults");
        templateHomepage.setAttribute("loginModal",group.getInstanceOf("loginModal"));
        templateHomepage.setAttribute("SIMILARITY_SEARCH_RESULTS", similaritySearchResults);
        return templateHomepage.toString();
    }

    public String getResults(String json)
    {
        StringTemplate templateResults = group.getInstanceOf("results4");
        List<Document> results = MorphoApplication.searcher.performSearch(json);

        if (results.size() == 0)
        {
            templateResults.setAttribute("mainResult", "");
            templateResults.setAttribute("texto", "<h1>No results found.</h1><br>We were not able to find any similar insects.");
            templateResults.setAttribute("bolitas", "");
            templateResults.setAttribute("extraResults", "");
            return templateResults.toString();
        }

        String r = results.get(0).getString("_id");

        String mainResult = "<img id=\"mainResult\" src=\"./assets/images/" + r.split("C")[0] + "/Composition"
                + r.substring(r.lastIndexOf('C')+1) + ".png"
                +"\" alt=\"Main Result\" style=\"max-width:100%;\" />";

        MorphoApplication.logger.info("Main result: " + mainResult);

        Document mainResultAttributes = results.get(0).get("attributes", Document.class);

        Document mainResultsOptional = mainResultAttributes.get("optional", Document.class);

        String text = "<h1>" + mainResultAttributes.getString("Scientific Name") + "</h1><br>";
        for(String key : mainResultAttributes.keySet())
        {
            if (!key.equals("Scientific Name") && !key.equals("optional"))
                text += key + ": " + mainResultAttributes.get(key).toString() + "<br>";
        }

        for(String key: mainResultsOptional.keySet()){
            text += key + ": " + mainResultsOptional.get(key).toString() + "<br>";
        }

        String bolitas2 = "";

        int photosPages;

        String compositionPhotos = "";

        try {
            JSONObject docData = (JSONObject) new JSONParser().parse(results.get(0).toJson());
            JSONArray photos = (JSONArray) docData.get("images");

            bolitas2 = "<li data-target=\"#photosCarousel\" data-slide-to=\"0\" class=\"active\"></li>";

            if(photos.size() >= 3){
                photosPages = ((int)(photos.size()/3));
            }else{
                photosPages = 1;
            }

            for (int i = 1; i < photos.size(); i++) {
                bolitas2 += "<li data-target=\"#photosCarousel\" data-slide-to=\"" + i + "\"></li>\n";
            }

            int i = 0;

            while(i < photosPages)
            {
                compositionPhotos += "<div class=\"item " + ((i==0)? "active" : "") + "\">\n" +
                        "<div class=\"row-fluid\">\n";
                int j = 0;
                while (i*3+j < photos.size())
                {
                    JSONObject o = (JSONObject) new JSONParser().parse(photos.get(j).toString());
                    String src = "." + o.get("image").toString();
                    compositionPhotos += "<div class=\"col-md-3 col-sm-3 col-lg-3\">"
                            + "<a class=\"thumbnail\"><img src=\""
                            + src
                            + "\" alt=\"Image\" style=\"max-width:75%;\" /></a></div>\n";
                    j++;
                }

                compositionPhotos += "</div><!--/row-fluid-->\n" +
                        "</div><!--/item-->";

                i++;
            }
        }catch (ParseException e){

        }

        String bolitas = "<li data-target=\"#myCarousel\" data-slide-to=\"0\" class=\"active\"></li>";

        int pages = ((int)(results.size()/3));

        MorphoApplication.logger.info("result pages: " + pages);

        for (int i = 1; i < pages; i++) {
            bolitas += "<li data-target=\"#myCarousel\" data-slide-to=\"" + i + "\"></li>\n";
        }

        int i = 0;

        String extraResults = "";

        while(i < pages)
        {
            extraResults += "<div class=\"item " + ((i==0)? "active" : "") + "\">\n" +
                    "<div class=\"row-fluid\">\n";
            int j = 0;
            while (i*3+j < results.size())
            {
                String rr = results.get(j++).getString("_id");
                String src = "./assets/images/" + rr.split("C")[0] + "/Composition"
                        + rr.substring(rr.lastIndexOf('C')+1) + ".png";
                extraResults += "<div class=\"col-md-3 col-sm-3 col-lg-3\">"
                        + "<a onclick=\"setMainResult(&quot;"
                        + rr + "&quot;, &quot;"
                        + src + "&quot;);\" class=\"thumbnail\"><img src=\""
                        + src
                        + "\" alt=\"Image\" style=\"max-width:100%;\" /></a></div>\n";
                j++;
            }

            extraResults += "</div><!--/row-fluid-->\n" +
                    "</div><!--/item-->";

            i++;
        }

        templateResults.setAttribute("mainResult", mainResult);
        templateResults.setAttribute("texto", text);
        templateResults.setAttribute("bolitas", bolitas);
        templateResults.setAttribute("extraResults", extraResults);
        templateResults.setAttribute("bolitas2", bolitas2);
        templateResults.setAttribute("compositionPhotos", compositionPhotos);
        return templateResults.toString();
    }

    public String search(String searchType){
        StringTemplate searchBase = group.getInstanceOf("searchBase");

        return searchBase.toString();
    }
}
