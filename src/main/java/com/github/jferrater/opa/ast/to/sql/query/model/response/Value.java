package com.github.jferrater.opa.ast.to.sql.query.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "value"
})
public class Value {

    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Object object;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValues() {
        return object;
    }

    public void setValues(Object values) {
        this.object = values;
    }
}
