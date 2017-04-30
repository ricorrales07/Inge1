package com.morpho;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.*;
import org.hibernate.validator.constraints.NotEmpty;
import io.dropwizard.lifecycle.*;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class MorphoConfiguration extends Configuration{
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    @Valid
    @NotNull
    @JsonProperty
    private JerseyClientConfiguration httpClient = new JerseyClientConfiguration();

    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return httpClient;
    }
    @JsonProperty
    @NotEmpty
    public String mongohost = "localhost";

    @JsonProperty
    public int mongoport = 27017;

    @JsonProperty
    @NotEmpty
    public String mongodb = "mydb";
}
