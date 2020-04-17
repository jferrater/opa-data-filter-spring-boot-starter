package com.github.jferrater.opa.ast.db.query.service;

import com.github.jferrater.opa.ast.db.query.config.PartialRequestConfig;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class DefaultPartialRequest {

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
        Map<String, Object> inputFromConfig = partialRequestConfig.getInput();
        if(inputFromConfig != null) {
            input.putAll(inputFromConfig);
        }
        partialRequest.setInput(input);
        return partialRequest;
    }

    private List<String> getHttpPaths() {
        final String httpPath = httpServletRequest.getServletPath();
        List<String> paths = new ArrayList<>(Arrays.asList(httpPath.split("/")));
        paths.removeAll(Collections.singletonList(""));
        return paths;
    }
}
