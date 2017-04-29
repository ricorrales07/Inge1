package com;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class ViewCreator {
    StringTemplateGroup group = new StringTemplateGroup("templates",".\\src\\main\\resources\\assets\\templates\\");
    public ViewCreator(){

    }

    public String getLoginPage(){
        StringTemplate templateLogin = group.getInstanceOf("login");
        templateLogin.setAttribute("ESTUDIANTE","lol");
        return templateLogin.toString();
    }
}
