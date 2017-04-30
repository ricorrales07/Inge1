package com.morpho.views;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class ViewCreator {

    public ViewCreator(){

    }

    public String getSamplePage(){ return new SampleView().getView(); }
}
