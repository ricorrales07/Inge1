package com.morpho.views;

import java.util.*;

import com.morpho.MorphoApplication;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import sun.awt.ModalExclude;

import org.bson.Document;

//import javax.swing.text.Document;
import javax.validation.constraints.Max;

/**
 * Created by Irvin Umaña on 29/4/2017.
 * This class contains different methods that build the different templates (views)
 * These methods are usually called inside a resource request to build a page, or any other HTML.
 */
public class ViewCreator {
    final StringTemplateGroup group = new StringTemplateGroup("templates","./templates");

    public ViewCreator(){

    }

    //JUST AN EXAMPLE.
    public String getSamplePage(){
        StringTemplate templateSample = group.getInstanceOf("sample");
        templateSample.setAttribute("ESTUDIANTE","lol");
        return templateSample.toString();
    }

    /**
     * Generates the html needed for the createPiecePage.
     * @return HTML of the page in a string object.
     */
    public String getCreatePiecePage(){
        StringTemplate templateSample = group.getInstanceOf("createPiece");
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
        return templateSample.toString();
    }

    /**
     * Generate the html needed for the home page.
     * @return HTML of the page in a string object.
     */
    public String getHomepage()
    {
        StringTemplate templateHomepage = group.getInstanceOf("homepage" );
        return templateHomepage.toString();
    }

    public String getResults(String json)
    {
        StringTemplate templateResults = group.getInstanceOf("results4" );
        List<Document> results = MorphoApplication.searcher.performSearch(json);

        String r = results.get(0).getString("_id");

        String mainResult = "<img id=\"mainResult\" src=\"./assets/images/Composition"
                + r.substring(r.lastIndexOf('C')+1) + ".png"
                +"\" alt=\"Main Result\" style=\"max-width:100%;\" />";

        MorphoApplication.logger.info("Main result: " + mainResult);

        String text = "";
        for(String key : results.get(0).keySet())
        {
            if (!(/*key != "_id" &&*/ key.equals("pieces") || key.equals("searchId") || key.equals("imgSource") || key.equals("images")))
                text += key + ": " + results.get(0).get(key).toString() + "\n";
        }

        String bolitas = "<!--li data-target=\"#myCarousel\" data-slide-to=\"0\" class=\"active\"></li>";

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
                extraResults += "<div class=\"col-md-3 col-sm-3 col-lg-3\"><a href=\"#x\" class=\"thumbnail\"><img src=\"./assets/images/Composition"
                    + rr.substring(rr.lastIndexOf('C')+1) + ".png"
                    +"\" alt=\"Image\" style=\"max-width:100%;\" /></a></div>\n";
            }

            extraResults += "</div><!--/row-fluid-->\n" +
                    "</div><!--/item-->";

            i++;
        }

        templateResults.setAttribute("mainResult", mainResult);
        templateResults.setAttribute("texto", text);
        templateResults.setAttribute("bolitas", bolitas);
        templateResults.setAttribute("extraResults", extraResults);
        return templateResults.toString();
    }
}
