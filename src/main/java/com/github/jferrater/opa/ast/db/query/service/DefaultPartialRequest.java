package com.github.jferrater.opa.ast.db.query.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.opa.ast.db.query.config.PartialRequestConfig;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
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
     *  as 'method' and 'path' properties in the Partial Request input.
     *
     * @return {@link PartialRequest} The default partial request
     */
    public PartialRequest getDefaultPartialRequest() {
        String query = partialRequestConfig.getQuery();
        if(query == null) {
            return null;
        }
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setQuery(query);
        Set<String> unknowns = partialRequestConfig.getUnknowns();
        if(unknowns != null) {
            partialRequest.setUnknowns(unknowns);
        }
        Map<String, Object> input = new HashMap<>();
        final String httpMethod = httpServletRequest.getMethod();
        input.put("method", httpMethod);
        List<String> paths = getHttpPaths();
        input.put("path", paths);
        input.put("subject", new CurrentUser("alice", "SOMA"));
        Map<String, Object> inputFromConfig = partialRequestConfig.getInput();
        if(inputFromConfig != null) {
            input.putAll(inputFromConfig);
        }
        partialRequest.setInput(input);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String asString = objectMapper.writeValueAsString(partialRequest);
            LOGGER.info("Partial request\n{}", asString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return partialRequest;
    }

    private List<String> getHttpPaths() {
        final String httpPath = httpServletRequest.getServletPath();
        List<String> paths = new ArrayList<>(Arrays.asList(httpPath.split("/")));
        paths.removeAll(Collections.singletonList(""));
        return paths;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class CurrentUser {
        String user;
        String location;

        public CurrentUser(String user, String location) {
            this.user = user;
            this.location = location;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
