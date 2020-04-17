package com.github.jferrater.opa.ast.db.query.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "opa.authorization")
@Data
public class OpaConfig {

    /**
     * Enable OPA data filter authorization
     */
    private boolean dataFilterEnabled = true;
    // Set default for localhost suitable for sidecar pattern deployment
    /**
     * The OPA compile API endpoint
     */
    private String url = "http://localhost:8181/v1/compile";
    private Datasource datasource;

}
