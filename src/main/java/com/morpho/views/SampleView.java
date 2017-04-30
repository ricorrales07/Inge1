package com.morpho.views;

import com.morpho.views.view_elements.Login;
import org.antlr.stringtemplate.StringTemplate;

/**
 * Created by Gabriel on 4/30/2017.
 */
public class SampleView extends View {
    private final Login login;

    public SampleView() {
        login = new Login();
        name = "sample";
    }

    public String getView() {
        StringTemplate templateSample = group.getInstanceOf("sample");
        templateSample.setAttribute("ESTUDIANTE","lol");
        templateSample.setAttribute("LS", login.getLoginScript());
        templateSample.setAttribute("LB", login.getLoginButton());
        return templateSample.toString();
    }
}
