package opa.datafilter.core.ast.db.query.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author joffryferrater
 */
@Configuration
@ConfigurationProperties(prefix = "opa.authorization")
public class OpaConfig {

    /**
     * Enable OPA data filter authorization
     */
    private boolean dataFilterEnabled = true;
    /**
     * The OPA compile API endpoint. Set default for localhost suitable for sidecar pattern deployment
     */
    private String url = "http://localhost:8181/v1/compile";

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
