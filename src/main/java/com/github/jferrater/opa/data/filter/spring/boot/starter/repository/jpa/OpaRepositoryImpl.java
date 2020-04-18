package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import com.github.jferrater.opa.ast.db.query.exception.PartialEvauationException;
import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements OpaRepository<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpaRepositoryImpl.class);

    @Resource(name = "defaultPartialRequest")
    private OpaClientService opaClientService;
    private final EntityManager entityManager;

    public OpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public OpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em);
        LOGGER.info("here second constructor");
    }

    @Override
    public List<T> filteredResults() {
        LOGGER.info("Entering findAll()");
        String sqlStatements = opaClientService.getExecutableSqlStatements();
        LOGGER.info("SQL statements: {}", sqlStatements);
        checkForCriterias(sqlStatements);
        LOGGER.info("Returning query");
        return getQuery(sqlStatements).getResultList();
    }


    private void checkForCriterias(String sqlStatements) {
        if(!sqlStatements.contains("WHERE")) {
            throw new PartialEvauationException("OPA Partial Evaluation response returns an empty criterias");
        }
    }

    protected TypedQuery<T> getQuery(String sqlStatements) {
        return entityManager.createQuery(sqlStatements, this.getDomainClass());
    }
}
