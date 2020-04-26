package com.github.jferrater.opadatafiltermongospringbootstarter.query;

import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.service.DefaultPartialRequest;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MongoQueryService<T> {

    @Qualifier("opaClientService")
    private OpaClientService<T> opaClientService;
    @Resource(name = "defaultPartialRequest")
    private DefaultPartialRequest defaultPartialRequest;

    public MongoQueryService(OpaClientService<T> opaClientService, DefaultPartialRequest defaultPartialRequest) {
        this.opaClientService = opaClientService;
        this.defaultPartialRequest = defaultPartialRequest;
    }

    /**
     * Sends the default {@link PartialRequest} to the Open Policy Agent server and receives the response.
     * The response is translated into  MongoDB query
     *
     * @return {@link Query} Returns the MongoDB query
     */
    public Query getMongoDBQuery() {
        OpaCompilerResponse opaCompilerApiResponse = opaClientService.getOpaCompilerApiResponse();
        AstToMongoDBQuery astToMongoDBQuery = new AstToMongoDBQuery(opaCompilerApiResponse);
        return astToMongoDBQuery.createQuery();
    }

    /**
     * Sends the {@link PartialRequest} to the Open Policy Agent server and receives the response.
     * The response is translated into  MongoDB query
     *
     * @param partialRequest {@link PartialRequest}
     * @return {@link Query} Returns the MongoDB query
     */
    public Query getMongoDBQuery(PartialRequest partialRequest) {
        OpaCompilerResponse opaCompilerResponse = opaClientService.getOpaCompilerApiResponse(partialRequest);
        AstToMongoDBQuery astToMongoDBQuery = new AstToMongoDBQuery(opaCompilerResponse);
        return astToMongoDBQuery.createQuery();
    }
}
