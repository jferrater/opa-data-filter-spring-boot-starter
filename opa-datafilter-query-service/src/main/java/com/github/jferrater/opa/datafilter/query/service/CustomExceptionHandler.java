package com.github.jferrater.opa.datafilter.query.service;

import opa.datafilter.core.ast.db.query.exception.OpaClientException;
import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(OpaClientException.class)
    public ResponseEntity<QueryResponse> handleOpaClientException(OpaClientException e) {
        ApiError apiError = new ApiError(500, e.getMessage());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setErrors(List.of(apiError));
        return new ResponseEntity<>(queryResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PartialEvauationException.class)
    public ResponseEntity<QueryResponse> handlePartialEvaluationException(PartialEvauationException e) {
        ApiError apiError = new ApiError(500, e.getMessage());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setErrors(List.of(apiError));
        return new ResponseEntity<>(queryResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<QueryResponse> handleConversionFailedException(ConversionFailedException e) {
        ApiError apiError = new ApiError(400, "Invalid request param : " + e.getMessage());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setErrors(List.of(apiError));
        return new ResponseEntity<>(queryResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<QueryResponse> handleIllegalArgumentExceptionException(IllegalArgumentException e) {
        ApiError apiError = new ApiError(400, "Invalid request param : " + e.getMessage());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setErrors(List.of(apiError));
        return new ResponseEntity<>(queryResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<QueryResponse> handleException(Exception e) {
        ApiError apiError = new ApiError(500, e.getMessage());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setErrors(List.of(apiError));
        return new ResponseEntity<>(queryResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
