package com.github.jferrater.opa.ast.db.query.service;

import com.github.jferrater.opa.ast.db.query.config.PartialRequestConfig;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultPartialRequestTest {

    private static final String X_ORG_HEADER = "X-ORG";
    private static final String AUTH_BASIC_HEADER = "Basic YWxpY2U6cGFzc3dvcmQ="; // base64 of alice:password
    private static final String QUERY = "data.allow==true";
    private static final String DATA_PETS = "data.pets";
    private static final String USERNAME_KEY = "username";
    private static final String USERNAME_VALUE = "alice";
    private static final String GET = "GET";
    private static final String PATH = "/pets";

    @Test
    void shouldCreateDefaultPartialRequest() {
        final HttpServletRequest mockHttpServletRequest = mock(HttpServletRequest.class);
        when(mockHttpServletRequest.getHeader(X_ORG_HEADER)).thenReturn("SOMA");
        when(mockHttpServletRequest.getHeader("Authorization")).thenReturn(AUTH_BASIC_HEADER);
        when(mockHttpServletRequest.getMethod()).thenReturn(GET);
        when(mockHttpServletRequest.getServletPath()).thenReturn(PATH);
        PartialRequestConfig partialRequestConfig = createConfig();
        DefaultPartialRequest target = new DefaultPartialRequest(mockHttpServletRequest, partialRequestConfig);

        PartialRequest result = target.getDefaultPartialRequest();

        assertThat(result, is(notNullValue()));
        assertThat(result.getQuery(), is(QUERY));
        assertThat(result.getUnknowns(), hasItem(DATA_PETS));
        Map<String, Object> inputResult = result.getInput();
        assertThat(inputResult.get("subject"), is(notNullValue()));
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
        partialRequestConfig.setLogPartialRequest(true);
        partialRequestConfig.setQuery(QUERY);
        partialRequestConfig.setUnknowns(Set.of(DATA_PETS));
        Map<String, String> userAttributes = new HashMap<>();
        userAttributes.put("clinic_location", "X-ORG");
        partialRequestConfig.setUserAttributeToHttpHeaderMap(userAttributes);
        return partialRequestConfig;
    }
}