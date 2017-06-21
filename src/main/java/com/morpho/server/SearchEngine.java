package com.morpho.server;

import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.util.JSON;
import com.morpho.MorphoApplication;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.bson.Document;

//import be.tarsos.lsh.Index;
//import be.tarsos.lsh;

import java.util.Collections;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by irvin on 29/4/2017.
 * This is the class that will search similarities is a pair of images, or a pair of Pieces Instances
 * or a pair of Composition Instance.
 */
public class SearchEngine {

    Map<String, Integer> propertiesKeys;

    MessageDigest digester;

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

    public String addSearchIdToPart(String pieceJson)
    {
        digester.update(pieceJson.getBytes());
        byte[] hashValue = digester.digest();

        Document piece = Document.parse(pieceJson);
        piece.put("searchId", hashValue);
        return piece.toJson();
    }

    public String addSearchIdToComposition(String compositionJson)
    {
        Document composition = Document.parse(compositionJson);

        List<Integer> searchId = new ArrayList<Integer>();
        for (Object part : composition.values())
        {
            searchId.add(((Document)part).getInteger("searchId")); //no estoy seguro de esto
        }

        Collections.sort(searchId);

        composition.put("searchId", searchId);

        return composition.toJson();
    }

    public List<Document> searchSimilarCompositions(Document composition, int pageNum)
    {
        List<Document> results = new ArrayList<Document>();

        composition = Document.parse(addSearchIdToComposition(composition.toJson()));

        FindIterable<Document> partialResults;

        ArrayList<Integer> searchCriteria = (ArrayList<Integer>) composition.get("searchId");

        int closeness = searchCriteria.size();

        int skip = pageNum * 10;

        do {
            try {
                partialResults = MorphoApplication.DBA.search("composition",
                        "{{searchId : {$slice : " + closeness + "}} : "
                                + searchCriteria.toString() + "}");

                for (Document result : partialResults)
                {
                    if (--skip <= 0) {
                        results.add(result);
                        if (results.size() >= 10)
                            break;
                    }
                }
            }
            catch (Exception e)
            {
                //log
            }

            closeness--;
            searchCriteria = (ArrayList<Integer>) searchCriteria.subList(0, closeness);
        } while (results.size() < 10 && closeness >= 0);

        return results;
    }
}
