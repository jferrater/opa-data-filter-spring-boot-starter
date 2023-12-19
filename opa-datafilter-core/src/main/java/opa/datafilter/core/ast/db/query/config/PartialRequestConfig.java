package opa.datafilter.core.ast.db.query.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * @author joffryferrater
 */
@Configuration
@ConfigurationProperties(prefix = "opa.partial-request")
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

    public PartialRequestConfig() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, String> getUserAttributeToHttpHeaderMap() {
        return userAttributeToHttpHeaderMap;
    }

    public void setUserAttributeToHttpHeaderMap(Map<String, String> userAttributeToHttpHeaderMap) {
        this.userAttributeToHttpHeaderMap = userAttributeToHttpHeaderMap;
    }

    public Set<String> getUnknowns() {
        return unknowns;
    }

    public void setUnknowns(Set<String> unknowns) {
        this.unknowns = unknowns;
    }

    public boolean isLogPartialRequest() {
        return logPartialRequest;
    }

    public void setLogPartialRequest(boolean logPartialRequest) {
        this.logPartialRequest = logPartialRequest;
    }
}
