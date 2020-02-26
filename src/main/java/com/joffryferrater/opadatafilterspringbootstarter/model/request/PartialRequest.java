package com.joffryferrater.opadatafilterspringbootstarter.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "query",
        "input",
        "unknowns"
})
public class PartialRequest {

    @JsonProperty("query")
    private String query;
    @JsonProperty("input")
    Map<String, Object> input = new HashMap<>();
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