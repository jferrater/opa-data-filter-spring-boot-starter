package opa.datafilter.core.ast.db.query.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import opa.datafilter.core.ast.db.query.deserializer.PredicateDeserializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joffryferrater
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "index",
        "terms"
})
@JsonDeserialize(using = PredicateDeserializer.class)
public class Predicate {

    @JsonProperty("index")
    private int index;
    @JsonProperty("terms")
    private List<Term> terms = new ArrayList<>();

    public Predicate() {
    }

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
