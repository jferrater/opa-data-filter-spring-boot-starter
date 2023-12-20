package com.github.jferrater.opadatafilterjpaspringbootstarter.query;

import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Component
public class QueryService<T> {

    @Qualifier("opaClientService")
    private OpaClientService opaClientService;

    public QueryService(OpaClientService opaClientService) {
        this.opaClientService = opaClientService;
    }

    public <S extends T> TypedQuery<S> getTypedQuery(Class<S> domainClass, Sort sort, EntityManager entityManager){
        OpaCompilerResponse opaCompilerResponse = opaClientService.getOpaCompilerApiResponse();
        TypedQueryBuilder<S> typedQueryBuilder = new TypedQueryBuilder<>(opaCompilerResponse, entityManager);
        return typedQueryBuilder.getTypedQuery(domainClass, sort);
    }

    public <S extends T> TypedQuery<S> getTypedQuery(Class<S> domainClass, Sort sort, EntityManager entityManager, PartialRequest partialRequest){
        OpaCompilerResponse opaCompilerResponse = opaClientService.getOpaCompilerApiResponse(partialRequest);
        TypedQueryBuilder<S> typedQueryBuilder = new TypedQueryBuilder<>(opaCompilerResponse, entityManager);
        return typedQueryBuilder.getTypedQuery(domainClass, sort);
    }
}
