package com.github.jferrater.opa.ast.to.sql.query.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "value"
})
public class Term {

    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Object value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(Object object) {
        this.value = object;
    }

    public Object getValue() {
        return value;
    }
}
