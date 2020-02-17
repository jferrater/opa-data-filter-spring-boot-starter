package com.joffryferrater.opadatafilterspringbootstarter.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    @JsonProperty("queries")
    private List<List<Query>> queries = new ArrayList<>();

    public List<List<Query>> getQueries() {
        return queries;
    }

    public void setQueries(List<List<Query>> queries) {
        this.queries = queries;
    }
}
