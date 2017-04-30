package com.morpho.views.view_elements;

import org.antlr.stringtemplate.StringTemplate;

/**
 * Created by Gabriel on 4/29/2017.
 */
public class Login {
    private final String loginScript = "<script>(function(d, s, id) {\n" +
            "          var js, fjs = d.getElementsByTagName(s)[0];\n" +
            "          if (d.getElementById(id)) return;\n" +
            "          js = d.createElement(s); js.id = id;\n" +
            "          js.src = \"//connect.facebook.net/es_LA/sdk.js#xfbml=1&version=v2.9&appId=212006762635772\";\n" +
            "          fjs.parentNode.insertBefore(js, fjs);\n" +
            "        }(document, 'script', 'facebook-jssdk'));</script>";
    private final String loginButton = "<div class=\"fb-login-button\" data-max-rows=\"1\" data-size=\"large\" data-button-type=\"login_with\" data-show-faces=\"false\" data-auto-logout-link=\"true\" data-use-continue-as=\"false\"></div>";

    public Login() {

    }

    public String getLoginScript() { return loginScript; }
    public String getLoginButton() { return loginButton; }
}
