package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import com.github.jferrater.opa.ast.db.query.exception.PartialEvauationException;
import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@NoRepositoryBean
public class OpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements OpaRepository<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpaRepositoryImpl.class);

    private final OpaClientService opaClientService;
    private final EntityManager entityManager;

    public OpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager, OpaClientService opaClientService) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.opaClientService = opaClientService;
    }

    public OpaRepositoryImpl(Class<T> domainClass, EntityManager em, OpaClientService opaClientService) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em, opaClientService);
        LOGGER.info("here second constructor");
    }

    @Override
    public List<T> findAll() {
        LOGGER.trace("Entering findAll()");
        LOGGER.trace("OpaClientService bean: {}", opaClientService);
        String sqlStatements = opaClientService.getExecutableSqlStatements();
        LOGGER.trace("SQL statements: {}", sqlStatements);
        checkForCriterias(sqlStatements);
        LOGGER.trace("ClassName: {}", this.getDomainClass().getName());
        List<T> resultList = getQuery(sqlStatements).getResultList();
        LOGGER.trace("Result list size: {}", resultList.size());
        return resultList;
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
