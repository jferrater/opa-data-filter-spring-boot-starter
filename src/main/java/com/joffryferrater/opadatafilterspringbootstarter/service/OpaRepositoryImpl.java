package com.joffryferrater.opadatafilterspringbootstarter.service;

import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class OpaRepositoryImpl<T> implements OpaRepository<T> {

    @PersistenceContext
    EntityManager entityManager;
    private OpaClientService opaClientService;

    public OpaRepositoryImpl(OpaClientService opaClientService) {
        this.opaClientService = opaClientService;
    }

    @Override
    public List<T> findAll(PartialRequest partialRequest) {
        String sqlQuery = opaClientService.getExecutableSqlStatements(partialRequest);
        Query query = entityManager.createNativeQuery(sqlQuery, Class.class);
        return query.getResultList();
    }
}
