package com.morpho.views;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

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
}
