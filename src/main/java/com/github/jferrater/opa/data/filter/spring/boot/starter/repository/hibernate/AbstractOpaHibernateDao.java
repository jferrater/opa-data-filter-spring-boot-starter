package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.hibernate;

import com.github.jferrater.opa.ast.db.query.exception.PartialEvauationException;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractOpaHibernateDao<T> {

    @Autowired
    SessionFactory sessionFactory;
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
     * Send query to the database using the default {@link PartialRequest} created by {@link com.github.jferrater.opa.ast.db.query.service.DefaultPartialRequest}
     * Use this method if you have a {@link com.github.jferrater.opa.ast.db.query.config.PartialRequestConfig} configured in the application.yml or application.properties
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
        NativeQuery<T> nativeQuery = getCurrentSession().createNativeQuery(sqlStatements, clazz);
        return nativeQuery.list();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}