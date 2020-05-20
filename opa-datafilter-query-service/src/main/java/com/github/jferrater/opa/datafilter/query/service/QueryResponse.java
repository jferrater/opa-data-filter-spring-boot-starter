package com.github.jferrater.opa.datafilter.query.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author joffryferrater
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {

    @JsonProperty("result")
    private Map<String, Object> result;
    @JsonProperty("errors")
    private List<ApiError> errors = new ArrayList<>();
}
