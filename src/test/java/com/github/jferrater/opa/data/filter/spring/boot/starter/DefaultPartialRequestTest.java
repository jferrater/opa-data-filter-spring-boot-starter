package com.github.jferrater.opa.data.filter.spring.boot.starter;

import com.github.jferrater.opa.ast.db.query.config.PartialRequestConfig;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.db.query.service.DefaultPartialRequest;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultPartialRequestTest {

    public static final String QUERY = "data.allow==true";
    public static final String DATA_PETS = "data.pets";
    public static final String USERNAME_KEY = "username";
    public static final String USERNAME_VALUE = "alice";
    public static final String GET = "GET";
    public static final String PATH = "/pets";

    @Test
    void shouldCreateDefaultPartialRequest() throws JsonProcessingException {
        final HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        when(mockHttpServletRequest.getMethod()).thenReturn(GET);
        when(mockHttpServletRequest.getServletPath()).thenReturn(PATH);
        PartialRequestConfig partialRequestConfig = createConfig();
        DefaultPartialRequest target = new DefaultPartialRequest(mockHttpServletRequest, partialRequestConfig);

        PartialRequest result = target.getDefaultPartialRequest();

        assertThat(result, is(notNullValue()));
        assertThat(result.getQuery(), is(QUERY));
        assertThat(result.getUnknowns(), hasItem(DATA_PETS));
        Map<String, Object> inputResult = result.getInput();
        assertThat(inputResult.get(USERNAME_KEY), is(USERNAME_VALUE));
        assertThat(inputResult.get("method"), is(GET));
        assertThat(inputResult.get("path"), is(notNullValue()));
    }

    @Test
    void shouldReturnNullIfQueryIsNotSetInfPartialRequestConfig() {
        final HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        when(mockHttpServletRequest.getMethod()).thenReturn(GET);
        when(mockHttpServletRequest.getServletPath()).thenReturn(PATH);
        DefaultPartialRequest target = new DefaultPartialRequest(mockHttpServletRequest, new PartialRequestConfig());

        PartialRequest result = target.getDefaultPartialRequest();

        assertThat(result, is(nullValue()));
    }

    private PartialRequestConfig createConfig() {
        PartialRequestConfig partialRequestConfig = new PartialRequestConfig();
        partialRequestConfig.setQuery(QUERY);
        partialRequestConfig.setUnknowns(Set.of(DATA_PETS));
        Map<String, Object> configInput = new HashMap<>();
        configInput.put(USERNAME_KEY, USERNAME_VALUE);
        partialRequestConfig.setInput(configInput);
        return partialRequestConfig;
    }
}