package com.github.jferrater.opa.ast.to.sql.query.exception;

public class OpaClientException extends RuntimeException {

    private ApiError apiError;

    public OpaClientException(ApiError apiError) {
        this.apiError = apiError;
    }

    public OpaClientException(String message) {
        super(message);
    }

    public ApiError getApiError() {
        return apiError;
    }

    public void setApiError(ApiError apiError) {
        this.apiError = apiError;
    }
}
