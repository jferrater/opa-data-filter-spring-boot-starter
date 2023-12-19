package opa.datafilter.core.ast.db.query.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

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
public class PartialRequest {

    private static final String METHOD = "method";
    private static final String PATH = "path";

    @JsonProperty("query")
    private String query;
    @JsonProperty("input")
    private Map<String, Object> input;
    @JsonProperty("unknowns")
    private Set<String> unknowns;

    public PartialRequest() {
    }

    public PartialRequest(String query, Map<String, Object> input, Set<String> unknowns) {
        this();
        this.query = query;
        this.input = input;
        this.unknowns = unknowns;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    public Set<String> getUnknowns() {
        return unknowns;
    }

    public void setUnknowns(Set<String> unknowns) {
        this.unknowns = unknowns;
    }

    public static class PartialRequestBuilder {

        private String query;
        private Map<String, Object> input = new HashMap<>();
        private Set<String> unknowns;


        public PartialRequestBuilder query(String query) {
            this.query = query;
            return this;
        }

        public PartialRequestBuilder input(String key, Object object) {
            this.input.put(key, object);
            return this;
        }

        public PartialRequestBuilder unknowns(Set<String> unknowns) {
            this.unknowns = unknowns;
            return this;
        }

        /**
         * Set the query for OPA to partially evaluate and compile
         *
         * @param method http methods
         * @return {@link PartialRequestBuilder}
         */
        public PartialRequestBuilder httpMethod(String method) {
            this.input.put(METHOD, method);
            return this;
        }

        /**
         * Add the list of HTTP path the current user want to access as input of
         * the Partial Request
         *
         * @param paths The list of http path or endpoint the user want to access
         * @return {@link PartialRequestBuilder}
         */
        public PartialRequestBuilder httpPath(List<String> paths) {
            this.input.put(PATH, paths);
            return this;
        }

        public PartialRequest build() {
            PartialRequest partialRequest = new PartialRequest();
            partialRequest.setQuery(this.query);
            partialRequest.setInput(this.input);
            partialRequest.setUnknowns(this.unknowns);
            return partialRequest;
        }
    }
}
