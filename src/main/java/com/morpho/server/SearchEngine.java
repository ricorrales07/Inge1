package com.morpho.server;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;
import com.morpho.MorphoApplication;
import org.bson.*;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import sun.security.ssl.Debug;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//import be.tarsos.lsh.Index;
//import be.tarsos.lsh;

import javax.print.Doc;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by irvin on 29/4/2017.
 * This is the class that will search similarities is a pair of images, or a pair of Pieces Instances
 * or a pair of Composition Instance.
 */
public class SearchEngine {

    Map<String, Integer> propertiesKeys;

    MessageDigest digester;
    Debug debugger;

    public SearchEngine(){
        /*********Irvin***********/
        //Until we identify other properties throught the set up of the rest
        // of this class, we are only using a few properties on
        //memory, not on the database.
        //TODO implement other propertyKeys in the DB.
        propertiesKeys = new HashMap<String, Integer>();
        propertiesKeys.put("type",0);
        propertiesKeys.put("author",1);
        propertiesKeys.put("part",2);
        propertiesKeys.put("color",3);

        /*********Ricardo***********/
        try
        {
            digester = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            MorphoApplication.logger.warning(e.toString());
        }
    }

    /**
     * Generate the Composition code based on its attributes.
     * Makes sure that the right codes are accessed.
     * @param attributes is the list of attributes related to the Composition.
     * @return String code that will be used to measure similarity.
     */
    public String getCompositionCode( List<String> attributes ){
        //Composition should call access for it's own propertiesKeys database.
        return attributeMapper(attributes);
    }

    /**
     * Generate the Piece code based on its attributes.
     * Makes sure that the right codes are accessed.
     * @param attributes is the list of attributes related to the Composition.
     * @return String code that will be used to measure similarity.
     */
    public String getPieceCode(  List<String> attributes  ){
        //Composition should call access for it's own propertiesKeys database.
        return attributeMapper(attributes);
    }

    /**
     * Generates the code for each instance of Piece or Composition based on the properties
     * using the database of such properties.
     * @param attributes is the list of attributes for the Piece or Composition.
     * @return String code that will be used to measure similarity.
     */
    private String attributeMapper( List<String> attributes ){
        String code = "";
        for (String attr: attributes) {
            code += propertiesKeys.containsKey(attr) ? propertiesKeys.get(attr) : "*";
        }
        return code;
    }

    public String addSearchIdToPiece(String data)
    {
        Document dataD = Document.parse(data);
        Document piece = (Document)dataD.get("piece");

        digester.update(piece.toJson().getBytes());
        ByteBuffer buffer = ByteBuffer.wrap(digester.digest());
        int hashValue = buffer.getInt();

        //Document piece = Document.parse(pieceJson);
        piece.put("searchId", hashValue);
        dataD.put("piece", piece);

        return dataD.toJson();
    }

    public String addSearchIdToComposition(String data)
    {
        Document dataD = Document.parse(data);
        Document composition = (Document) dataD.get("composition");
        ArrayList<Document> pieces = (ArrayList<Document>) composition.get("pieces");

        BsonArray searchId = new BsonArray();
        ArrayList<BsonInt32> preSearchId = new ArrayList<BsonInt32>();
        for (Document part : pieces)
        {
            String id = part.getString("_id");
            try {
                Document p = Document.parse(MorphoApplication.DBA.find("piece", "{_id: \"" + id + "\"}"));
                MorphoApplication.logger.info("piece searchId: " + p.getInteger("searchId").toString());
                preSearchId.add(new BsonInt32(p.getInteger("searchId")));
            }
            catch(Exception e)
            {
                MorphoApplication.logger.warning(e.toString());
            }
        }

        Collections.sort(preSearchId);

        for (int i = 0; i < preSearchId.size(); i++)
        {
            BsonArray sublist = new BsonArray();
            sublist.addAll(preSearchId.subList(0, i+1));
            searchId.add(sublist);
        }

        MorphoApplication.logger.info("composition searchId: " + searchId.toString());

        composition.put("searchId", searchId);
        dataD.put("composition", composition);

        return dataD.toJson();
    }

    public ArrayList<String> searchSimilarCompositions(String data, int pageNum)
    {
        ArrayList<String> results = new ArrayList<String>();

        //Document composition = (Document) Document.parse(addSearchIdToComposition(data)).get("composition");
        Document composition = (Document) Document.parse(data).get("composition");

        MorphoApplication.logger.info("composition: " + composition.toJson());

        FindIterable<Document> partialResults;

        ArrayList<ArrayList<Integer>> searchCriteria =
                (ArrayList<ArrayList<Integer>>) composition.get("searchId"); //THIS API'S DOCS SUCK!!

        /*System.out.println("" + preSearchCriteria);

        ArrayList<String> searchCriteria = preSearchCriteria.get(preSearchCriteria.size()-1);

        for (Object id : searchCriteria)
            System.out.println("Criterium: " + id);*/

        int closeness = searchCriteria.size()-1;

        int skip = (pageNum-1) * 10;

        do {
            MorphoApplication.logger.finer("Searching...");
            try {
                MorphoApplication.logger.fine("closeness: " + closeness);


                ArrayList<Integer> filter = searchCriteria.get(closeness);
                String temp = "[";
                for (Integer x : filter)
                    temp += x + ", ";
                if (temp.length() > 1)
                    temp = temp.substring(0, temp.length()-2);
                temp += "]";

                MorphoApplication.logger.fine("Search filter: {\"searchId." + closeness + "\" : "
                        + temp + "}");

                partialResults = MorphoApplication.DBA.search("composition",
                        "{\"searchId." + closeness + "\" : " + temp + "}");

                for (Document result : partialResults)
                {
                    if (--skip <= 0 && !results.contains(result.toJson())) {
                        results.add(result.toJson());
                        if (results.size() >= 10)
                            break;
                    }
                }
            }
            catch (Exception e)
            {
                MorphoApplication.logger.warning(e.toString());
            }

            closeness--;
            //if (closeness >= 0)
            //   searchCriteria.remove(closeness);
        } while (results.size() < 10 && closeness >= 0);

        return results;
    }

    public List<String> performSearch(String receivedContent)
    {
        MorphoApplication.logger.info(receivedContent);
        try {
            JSONObject receivedJSON = (JSONObject) new JSONParser().parse(receivedContent);
            JSONObject data = (JSONObject) new JSONParser().parse(receivedJSON.get("composition").toString());
            JSONObject id = (JSONObject) new JSONParser().parse(receivedJSON.get("auth").toString());
            try {
                data.put("_id", id.get("userID").toString() + "C0");
            }
            catch (NullPointerException e)
            {
                MorphoApplication.logger.warning("Facebook inaccessible.");
                data.put("_id", "0C0");
            }
            receivedJSON.put("composition", data);

            MorphoApplication.logger.info("Received content: " + receivedContent);
        } catch(ParseException e){
            MorphoApplication.logger.warning(e.toString());
            e.printStackTrace();
        }
        receivedContent = MorphoApplication.searcher.addSearchIdToComposition(receivedContent);


        ArrayList<String> results =
                MorphoApplication.searcher.searchSimilarCompositions(receivedContent, 1);

        return results;
    }
}
