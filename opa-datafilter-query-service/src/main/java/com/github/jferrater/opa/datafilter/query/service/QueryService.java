package com.github.jferrater.opa.datafilter.query.service;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import opa.datafilter.core.ast.db.query.exception.OpaClientException;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author joffryferrater
 */
@Service
public class QueryService {

    private OpaClientService opaClientService;
    private ModelMapper modelMapper;
    @SuppressWarnings("rawtypes")
    private MongoQueryService mongoQueryService;

    @SuppressWarnings("rawtypes")
    public QueryService(OpaClientService opaClientService, MongoQueryService mongoQueryService, ModelMapper modelMapper) {
        this.opaClientService = opaClientService;
        this.modelMapper = modelMapper;
        this.mongoQueryService = mongoQueryService;
    }

    public QueryResponse getSqlQuery(PartialRequest partialRequest) {
        try {
            String sqlStatements = opaClientService.getExecutableSqlStatements(convertToOpaPartialRequest(partialRequest));
            QueryResponse queryResponse = new QueryResponse();
            queryResponse.setResult(sqlStatements);
            return queryResponse;
        } catch (OpaClientException e) {
            throw new OpaClientException(e.getMessage());
        }
    }

    public QueryResponse getMongoDbQuery(PartialRequest partialRequest) {
        Query mongoDBQuery = mongoQueryService.getMongoDBQuery(convertToOpaPartialRequest(partialRequest));
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setResult(mongoDBQuery.getQueryObject().toJson());
        return queryResponse;
    }

    private opa.datafilter.core.ast.db.query.model.request.PartialRequest convertToOpaPartialRequest(PartialRequest partialRequest) {
        return modelMapper.map(partialRequest, opa.datafilter.core.ast.db.query.model.request.PartialRequest.class);
    }
}
