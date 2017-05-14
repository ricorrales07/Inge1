package com.morpho.views;

import com.morpho.views.SampleView;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class ViewCreator {
    final StringTemplateGroup group = new StringTemplateGroup("templates","./templates");

    public ViewCreator(){

    }

    public String getSamplePage(){ return new SampleView().getView(); }

    public String getCreatePiecePage(){
        StringTemplate templateSample = group.getInstanceOf("createPiece");
        templateSample.setAttribute("ESTUDIANTE","lol");
        return templateSample.toString();
    }

    public String getHomepage()
    {
        StringTemplate templateHomepage = group.getInstanceOf("homepage" );
        return templateHomepage.toString();
    }
}
