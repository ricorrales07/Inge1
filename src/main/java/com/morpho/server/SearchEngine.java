package com.morpho.server;

import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;
import com.morpho.MorphoApplication;
import org.bson.BsonArray;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.bson.Document;
import sun.security.ssl.Debug;

//import be.tarsos.lsh.Index;
//import be.tarsos.lsh;

import javax.print.Doc;
import java.io.UnsupportedEncodingException;
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
            //log
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
        byte[] hashValue = digester.digest();

        //Document piece = Document.parse(pieceJson);
        piece.put("searchId", new String(hashValue));
        dataD.put("piece", piece);

        return dataD.toJson();
    }

    public String addSearchIdToComposition(String data)
    {
        Document dataD = Document.parse(data);
        Document composition = (Document) dataD.get("composition");
        ArrayList<Document> pieces = (ArrayList<Document>) composition.get("pieces");

        BsonArray searchId = new BsonArray();
        ArrayList<BsonString> preSearchId = new ArrayList<BsonString>();
        for (Document part : pieces)
        {
            String id = part.getString("_id");
            try {
                Document p = Document.parse(MorphoApplication.DBA.find("piece", "{_id: \"" + id + "\"}"));
                System.out.println("piece searchId: " + p.getString("searchId"));
                preSearchId.add(new BsonString(p.getString("searchId")));
            }
            catch(Exception e)
            {
                //TODO: log
            }
        }

        Collections.sort(preSearchId);

        for (int i = 0; i < preSearchId.size(); i++)
        {
            BsonArray sublist = new BsonArray();
            sublist.addAll(preSearchId.subList(0, i+1));
            searchId.add(sublist);
        }

        System.out.println("composition searchId: " + searchId.toString());

        composition.put("searchId", searchId);
        dataD.put("composition", composition);

        return dataD.toJson();
    }

    public ArrayList<String> searchSimilarCompositions(String data, int pageNum)
    {
        ArrayList<String> results = new ArrayList<String>();

        //Document composition = (Document) Document.parse(addSearchIdToComposition(data)).get("composition");
        Document composition = (Document) Document.parse(data).get("composition");

        System.out.println("composition: " + composition.toJson());

        FindIterable<Document> partialResults;

        ArrayList<ArrayList<String>> searchCriteria =
                (ArrayList<ArrayList<String>>) composition.get("searchId"); //THIS API'S DOCS SUCK!!

        /*System.out.println("" + preSearchCriteria);

        ArrayList<String> searchCriteria = preSearchCriteria.get(preSearchCriteria.size()-1);

        for (Object id : searchCriteria)
            System.out.println("Criterium: " + id);*/

        int closeness = searchCriteria.size()-1;

        int skip = (pageNum-1) * 10;

        do {
            System.out.println("Searching...");
            try {
                System.out.println("closeness: " + closeness);


                ArrayList<String> filter = searchCriteria.get(closeness);
                String temp = "[";
                for (String x : filter)
                    temp += "\"" + x + "\", ";
                if (temp.length() > 1)
                    temp = temp.substring(0, temp.length()-2);
                temp += "]";

                System.out.println("Search filter: {\"searchId." + closeness + "\" : "
                        + temp + "}");

                partialResults = MorphoApplication.DBA.search("composition",
                        "{searchId" + closeness + " : " + temp + "}");

                for (Document result : partialResults)
                {
                    if (--skip <= 0) {
                        results.add(result.toJson());
                        if (results.size() >= 10)
                            break;
                    }
                }
            }
            catch (Exception e)
            {
                //log
                System.out.println(e.getMessage());
            }

            closeness--;
            if (closeness >= 0)
                searchCriteria.remove(closeness);
        } while (results.size() < 10 && closeness >= 0);

        return results;
    }
}
