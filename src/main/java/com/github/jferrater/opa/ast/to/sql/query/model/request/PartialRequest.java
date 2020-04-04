package com.github.jferrater.opa.ast.to.sql.query.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author joffryferrater
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "query",
        "input",
        "unknowns"
})
public class PartialRequest {

    @NotNull
    @JsonProperty("query")
    private String query;
    @JsonProperty("input")
    private Map<String, Object> input = new HashMap<>();
    @JsonProperty("unknowns")
    private Set<String> unknowns = new HashSet<>();

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public Set<String> getUnknowns() {
        return unknowns;
    }

    public void setUnknowns(Set<String> unknowns) {
        this.unknowns = unknowns;
    }
}
