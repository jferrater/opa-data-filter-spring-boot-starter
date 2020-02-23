package com.joffryferrater.opadatafilterspringbootstarter.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.joffryferrater.opadatafilterspringbootstarter.core.deserializer.QueryDeserializer;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "index",
        "terms"
})
@JsonDeserialize(using = QueryDeserializer.class)
public class Predicate {

    @JsonProperty("index")
    private int index;
    @JsonProperty("terms")
    private List<Term> terms = new ArrayList<>();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }
}
