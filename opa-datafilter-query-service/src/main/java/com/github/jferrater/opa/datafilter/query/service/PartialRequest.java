package com.github.jferrater.opa.datafilter.query.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

/**
 * @author joffryferrater
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class PartialRequest {

    @JsonProperty("query")
    @NotNull
    private String query;
    @JsonProperty("input")
    @NotNull
    private Map<String, Object> input;
    @NotNull
    @JsonProperty("unknowns")
    private Set<String> unknowns;
}
