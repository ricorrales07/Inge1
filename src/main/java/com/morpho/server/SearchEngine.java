package com.morpho.server;

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

    public SearchEngine(){
        //Until we identify other properties throught the set up of the rest
        // of this class, we are only using a few properties on
        //memory, not on the database.
        //TODO implement other propertyKeys in the DB.
        propertiesKeys = new HashMap<String, Integer>();
        propertiesKeys.put("type",0);
        propertiesKeys.put("author",1);
        propertiesKeys.put("part",2);
        propertiesKeys.put("color",3);

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
}
