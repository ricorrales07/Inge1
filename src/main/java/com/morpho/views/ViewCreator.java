package com.morpho.views;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class ViewCreator {
    final StringTemplateGroup group = new StringTemplateGroup("templates","./templates");

    public ViewCreator(){

    }

    public String getSamplePage(){
        StringTemplate templateSample = group.getInstanceOf("sample");
        templateSample.setAttribute("ESTUDIANTE","lol");
        return templateSample.toString();
    }

    public String getCreatePiecePage(){
        StringTemplate templateSample = group.getInstanceOf("createPiece");
        templateSample.setAttribute("ESTUDIANTE","lol");
        return templateSample.toString();
    }

    public String getEditPiecePage(String pieceId){
        // Con pieceId voy a la base de datos para obtener los datos.
        StringTemplate templateSample = group.getInstanceOf("createPiece"); //USA EL MISMO TEMPLATE
        templateSample.setAttribute("ESTUDIANTE","lol");  //Así como este atributo se puede meter las imagenes y
        //las propiedades de la pieza en la página. Tal vez en un div que tenga display:none. Despues con javascript
        //selecciono este div, le saco los datos que quiero, and "populate" las diferentes tarjetas y las vistas (surfaces en easel).
        return templateSample.toString();
    }

    public String getHomepage()
    {
        StringTemplate templateHomepage = group.getInstanceOf("homepage" );
        return templateHomepage.toString();
    }
}
