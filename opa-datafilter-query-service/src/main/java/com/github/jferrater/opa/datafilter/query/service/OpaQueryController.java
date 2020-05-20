package com.github.jferrater.opa.datafilter.query.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "query_service", description = "The OPA Data Filter Query API")
public class OpaQueryController {

    private QueryService queryService;

    public OpaQueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @Operation(
            summary = "Translates OPA partial request into an sql or mongodb query",
            description = "Returns a sql or mongodb query given an OPA partial request",
            tags = "query_service"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QueryResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QueryResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = QueryResponse.class)))
            }
    )
    @PostMapping(value = "/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QueryResponse> getQueryStatement(
            @Parameter(description = "Valid values: sql, mongodb") @RequestParam("type") String type,
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
