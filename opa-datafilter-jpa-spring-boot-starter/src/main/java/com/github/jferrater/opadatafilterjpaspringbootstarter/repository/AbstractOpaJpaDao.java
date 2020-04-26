package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import opa.datafilter.core.ast.db.query.exception.PartialEvauationException;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author joffryferrater
 * @param <T> The entity
 */
public abstract class AbstractOpaJpaDao<T> {

    @PersistenceContext(unitName = "entityManagerFactory")
    EntityManager entityManager;
    @Autowired
    OpaClientService opaClientService;

    private Class<T> clazz;

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Send query to the database. Use this method for customizing the {@link PartialRequest}
     *
     * @param partialRequest {@link PartialRequest}
     * @return {@link List} The filtered collection
     */
    public List<T> filterData(PartialRequest partialRequest) {
        String sqlStatements = opaClientService.getExecutableSqlStatements(partialRequest);
        checkForCriterias(sqlStatements);
        return getList(sqlStatements);
    }

    /**
     * Send query to the database using the default {@link PartialRequest} created by {@link opa.datafilter.core.ast.db.query.service.DefaultPartialRequest}
     * Use this method if you have a user attributes to Http header mappings configured in the application.yml or application.properties
     * of your Spring Boot project
     *
     * @return {@link List} The filtered collection
     */
    public List<T> filterData() {
        String sqlStatements = opaClientService.getExecutableSqlStatements();
        checkForCriterias(sqlStatements);
        return getList(sqlStatements);
    }

    private void checkForCriterias(String sqlStatements) {
        if(!sqlStatements.contains("WHERE")) {
            throw new PartialEvauationException("OPA Partial Evaluation response returns an empty criterias");
        }
    }

    private List<T> getList(String sqlStatements) {
        Query nativeQuery = entityManager.createNativeQuery(sqlStatements, clazz);
        return nativeQuery.getResultList();
    }

}
