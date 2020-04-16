package com.github.jferrater.opa.ast.db.query.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    @JsonProperty("queries")
    private List<List<Predicate>> queries = new ArrayList<>();

    public List<List<Predicate>> getQueries() {
        return queries;
    }

    public void setQueries(List<List<Predicate>> queries) {
        this.queries = queries;
    }
}
