package com.github.jferrater.opa.ast.to.sql.query.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "opa.authorization")
public class OpaConfig {

    private boolean dataFilterEnabled = true;
    private String url;

    public boolean isDataFilterEnabled() {
        return dataFilterEnabled;
    }

    public void setDataFilterEnabled(boolean dataFilterEnabled) {
        this.dataFilterEnabled = dataFilterEnabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
