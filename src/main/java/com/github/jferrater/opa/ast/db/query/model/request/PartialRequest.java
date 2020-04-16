package com.github.jferrater.opa.ast.db.query.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

import java.util.Map;
import java.util.Set;

import lombok.*;

/**
 * @author Reihmon Estremos
 * @author joffryferrater
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query",
    "input",
    "unknowns"
})
@Builder
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class PartialRequest {

    private static final String METHOD = "method";
    private static final String PATH = "path";

    @JsonProperty("query")
    private String query;
    @JsonProperty("input")
    @Singular("input")
    private final Map<String, Object> input;
    @JsonProperty("unknowns")
    @Singular
    private final Set<String> unknowns;

    public static class PartialRequestBuilder {

        /**
         * Set the query for OPA to partially evaluate and compile
         *
         * @param method http methods
         * @return {@link PartialRequestBuilder}
         */
        public PartialRequestBuilder httpMethod(String method) {
            return this.input(METHOD, method);
        }

        /**
         * Add the list of HTTP path the current user want to access as input of
         * the Partial Request
         *
         * @param paths The list of http path or endpoint the user want to access
         * @return {@link PartialRequestBuilder}
         */
        public PartialRequestBuilder httpPath(List<String> paths) {
            return this.input(PATH, paths);
        }
    }
}
