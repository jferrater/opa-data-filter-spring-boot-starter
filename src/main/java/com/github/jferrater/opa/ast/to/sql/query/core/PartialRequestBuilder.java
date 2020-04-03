package com.github.jferrater.opa.ast.to.sql.query.core;

import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;

import java.util.*;

/**
 * @author joffryferrater
 */
public class PartialRequestBuilder {

    private String query;
    private Map<String, Object> input = new HashMap<>();
    private Set<String> unknowns = new HashSet<>();

    public static PartialRequestBuilder getInstance() {
        return new PartialRequestBuilder();
    }

    /**
     * Set the query for OPA to partially evaluate and compile
     *
     * @param query The query to partially evaluate and compile
     * @return {@link PartialRequestBuilder}
     */
    public PartialRequestBuilder query(String query) {
        this.query = query;
        return this;
    }

    /**
     * Add the list of HTTP path the current user want to access
     *    as input of the Partial Request
     *
     * @param path The list of HTTP path a user want to access
     * @return {@link PartialRequestBuilder}
     */
    public PartialRequestBuilder httpPath(List<String> path){
        input.put("path", path);
        return this;
    }

    /**
     * Add the HTTP method of the path the current user want to access
     *   as input of the Partial Request
     *
     * @param httpMethod The HTTP method, e.g. 'GET', 'POST', 'PUT', 'DELETE'
     * @return {@link PartialRequestBuilder}
     */
    public PartialRequestBuilder httpMethod(String httpMethod) {
        input.put("method", httpMethod);
        return this;
    }

    /**
     * Add a new key value pair to the input of the Partial Request
     *
     * @param key The key
     * @param value The value
     * @return {@link PartialRequestBuilder}
     */
    public PartialRequestBuilder withInput(String key, Object value) {
        input.put(key, value);
        return this;
    }

    /**
     * Set the terms to treat as unknown during partial evaluation
     *
     * @param unknowns The terms to treat as unknown during partial evaluation
     * @return {@link PartialRequestBuilder}
     */
    public PartialRequestBuilder withUnknowns(Set<String> unknowns) {
        this.unknowns = unknowns;
        return this;
    }

    /**
     * Build the {@link PartialRequest} object
     *
     * @return {@link PartialRequest}
     */
    public PartialRequest build() {
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setQuery(query);
        partialRequest.setUnknowns(unknowns);
        partialRequest.setInput(input);
        return partialRequest;
    }
}
