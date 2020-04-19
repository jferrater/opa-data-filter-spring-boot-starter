package com.github.jferrater.opa.ast.db.query.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "opa.partial-request")
@Data
@NoArgsConstructor
public class PartialRequestConfig {

    /**
     * The query to partially evaluate and compile
     */
    private String query;
    /**
     * The mapping of user attribute to Http Header. These mappings will be added as subject properties in the input
     * of the partial request
     */
    private Map<String, String> userAttributeToHttpHeaderMap;
    /**
     * The terms to treat as unknown during partial evaluation(default: ["input"]
     */
    private Set<String> unknowns;
    /**
     * Log the partial request on std out for debugging
     */
    private boolean logPartialRequest = false;
}
