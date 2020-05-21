package com.github.jferrater.opa.datafilter.query.service;

import com.github.jferrater.opa.datafilter.query.service.model.QueryResponse;
import opa.datafilter.core.ast.db.query.exception.OpaClientException;
import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler target;

    @BeforeEach
    void setUp() {
        target = new CustomExceptionHandler();
    }

    @Test
    void shouldHandleOpaClientException() {
        ResponseEntity<QueryResponse> result = target.handleOpaClientException(new OpaClientException("opaclientexception"));

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(500));
    }

    @Test
    void shouldPartialEvaluationException() {
        ResponseEntity<QueryResponse> result = target.handlePartialEvaluationException(new PartialEvauationException("partialEvaluationExceptio"));

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(500));
    }

    @Test
    void shouldHandleConversionFailedException() {
        ResponseEntity<QueryResponse> result = target.handleConversionFailedException(new ConversionFailedException(null, null, null, new IllegalArgumentException("conversionFailedException")));

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(400));
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        ResponseEntity<QueryResponse> result = target.handleIllegalArgumentExceptionException(new IllegalArgumentException("illegalArgumentException"));

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(400));
    }

    @Test
    void shouldHandleException() {
        ResponseEntity<QueryResponse> result = target.handleException(new IOException("illegalArgumentException"));

        assertThat(result, is(notNullValue()));
        assertThat(result.getStatusCodeValue(), is(500));
    }
}