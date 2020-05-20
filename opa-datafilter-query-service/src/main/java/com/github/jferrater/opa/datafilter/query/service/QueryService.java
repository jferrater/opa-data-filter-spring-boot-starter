package com.github.jferrater.opa.datafilter.query.service;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author joffryferrater
 */
@Service
public class QueryService {

    private OpaClientService opaClientService;
    private ModelMapper modelMapper;
    private MongoQueryService mongoQueryService;

    public QueryService(OpaClientService opaClientService, MongoQueryService mongoQueryService, ModelMapper modelMapper) {
        this.opaClientService = opaClientService;
        this.modelMapper = modelMapper;
        this.mongoQueryService = mongoQueryService;
    }

    public QueryResponse getSqlQuery(PartialRequest partialRequest) {
        String sqlStatements = opaClientService.getExecutableSqlStatements(convertToOpaPartialRequest(partialRequest));
        Map<String, Object> result = Map.of("result", sqlStatements);
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setResult(result);
        return queryResponse;
    }

    public QueryResponse getMongoDbQuery(PartialRequest partialRequest) {
        Query mongoDBQuery = mongoQueryService.getMongoDBQuery(convertToOpaPartialRequest(partialRequest));
        Map<String, Object> result = Map.of("result", mongoDBQuery.getQueryObject().toJson());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setResult(result);
        return queryResponse;
    }

    private opa.datafilter.core.ast.db.query.model.request.PartialRequest convertToOpaPartialRequest(PartialRequest partialRequest) {
        return modelMapper.map(partialRequest, opa.datafilter.core.ast.db.query.model.request.PartialRequest.class);
    }
}
