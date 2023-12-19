package opa.datafilter.core.ast.db.query.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author joffryferrater
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpaCompilerResponse {

    @JsonProperty("result")
    private Result result;

    public OpaCompilerResponse() {
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
