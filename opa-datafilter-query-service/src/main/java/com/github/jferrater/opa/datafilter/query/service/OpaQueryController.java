package com.github.jferrater.opa.datafilter.query.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OpaQueryController {

    private QueryService queryService;

    public OpaQueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryResponse> getQueryStatement(
            @RequestParam("type") String type,
            @Valid @RequestBody PartialRequest partialRequest) {
        if("sql".equals(type)) {
            QueryResponse queryResponse = queryService.getSqlQuery(partialRequest);
            return new ResponseEntity<>(queryResponse, HttpStatus.OK);
        } else if("mongodb".equals(type)) {
            QueryResponse queryResponse = queryService.getMongoDbQuery(partialRequest);
            return new ResponseEntity<>(queryResponse, HttpStatus.OK);
        } else {
            QueryResponse queryResponse = new QueryResponse();
            ApiError apiError = new ApiError(400, "Invalid 'type' request param value. " +
                    "Valid request param values: sql , mongodb");
            queryResponse.setErrors(List.of(apiError));
            return new ResponseEntity<>(queryResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
