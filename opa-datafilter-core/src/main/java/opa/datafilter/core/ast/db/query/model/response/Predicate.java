package opa.datafilter.core.ast.db.query.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
public class Predicate {

    @JsonProperty("index")
    private int index;
    @JsonProperty("terms")
    private List<Term> terms = new ArrayList<>();
}
