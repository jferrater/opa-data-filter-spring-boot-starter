package com.github.jferrater.opa.ast.db.query.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.opa.ast.db.query.config.PartialRequestConfig;
import com.github.jferrater.opa.ast.db.query.model.Subject;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DefaultPartialRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPartialRequest.class);

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private PartialRequestConfig partialRequestConfig;

    public DefaultPartialRequest() {
        //
    }

    public DefaultPartialRequest(HttpServletRequest httpServletRequest, PartialRequestConfig partialRequestConfig) {
        this.httpServletRequest = httpServletRequest;
        this.partialRequestConfig = partialRequestConfig;
    }

    /**
     * Creates a default {@link PartialRequest} from the http servlet request and {@link PartialRequestConfig}. Added by default the http method and path
     * as 'method' and 'path' properties in the Partial Request input.
     *
     * @return {@link PartialRequest} The default partial request
     */
    public PartialRequest getDefaultPartialRequest() {
        String query = partialRequestConfig.getQuery();
        if (query == null) {
            return null;
        }
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setQuery(query);
        Set<String> unknowns = partialRequestConfig.getUnknowns();
        if (unknowns != null) {
            partialRequest.setUnknowns(unknowns);
        }
        Map<String, Object> input = createInputObj();
        partialRequest.setInput(input);
        if (partialRequestConfig.isLogPartialRequest()) {
            logPartialRequestJsonString(partialRequest);

        }
        return partialRequest;
    }

    private void logPartialRequestJsonString(PartialRequest partialRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String asString = objectMapper.writeValueAsString(partialRequest);
            LOGGER.info("Partial request in json:\n{}", asString);
        } catch (JsonProcessingException e) {
            LOGGER.warn("Error serializing partial request object, {}", e.getMessage(), e);
        }
    }

    private Map<String, Object> createInputObj() {
        Map<String, Object> input = new HashMap<>();
        final String httpMethod = httpServletRequest.getMethod();
        input.put("method", httpMethod);
        List<String> paths = getHttpPaths();
        input.put("path", paths);
        input.put("subject", createSubject());
        return input;
    }

    private List<String> getHttpPaths() {
        final String httpPath = httpServletRequest.getServletPath();
        List<String> paths = new ArrayList<>(Arrays.asList(httpPath.split("/")));
        paths.removeAll(Collections.singletonList(""));
        return paths;
    }

    private Subject createSubject() {
        Subject subject = new Subject();
        final String username = getUserName();
        subject.setUser(username);
        subject.setJwt(getJwtToken());
        Map<String, String> useAttributes = getUseAttributes();
        subject.setAttributes(useAttributes);
        return subject;
    }

    private String getUserName() {
        final String authorization = getAuthorizationHeader();
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);
            return values[0];
        }
        return null;
    }

    private String getJwtToken() {
        final String authorization = getAuthorizationHeader();
        if (authorization != null && authorization.toLowerCase().startsWith("bearer")) {
            return authorization.substring("Bearer".length()).trim();
        }
        return null;
    }

    private Map<String, String> getUseAttributes() {
        Map<String, String> userAttributeToHttpHeaderMap = Objects.requireNonNullElse(partialRequestConfig.getUserAttributeToHttpHeaderMap(), new HashMap<>());
        Map<String, String> userAttributes = new HashMap<>();
        userAttributeToHttpHeaderMap.forEach((k, v) -> {
            String header = getHeader(v);
            if(header != null) {
                userAttributes.put(k, header);
            }
        });
        return userAttributes;
    }

    private String getAuthorizationHeader() {
        return getHeader("Authorization");
    }

    private String getHeader(String header) {
        return httpServletRequest.getHeader(header);
    }
}
