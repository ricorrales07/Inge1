package com.morpho.views;

import org.antlr.stringtemplate.StringTemplateGroup;

/**
 * Created by Gabriel on 4/30/2017.
 */
abstract class View {
    StringTemplateGroup group = new StringTemplateGroup("templates",".\\src\\main\\resources\\assets\\templates\\");
    String name;

    abstract public String getView();
    public String getName() { return name; };
}
