package com.github.jferrater.opa.ast.db.query.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "opa.partial-request")
@Data
public class PartialRequestConfig {

    /**
     * The query to partially evaluate and compile
     */
    private String query;
    /**
     * The input document to use during partial evaluation
     */
    private Map<String, Object> input;
    /**
     * The terms to treat as unknown during partial evaluation(default: ["input"]
     */
    private Set<String> unknowns;
}
