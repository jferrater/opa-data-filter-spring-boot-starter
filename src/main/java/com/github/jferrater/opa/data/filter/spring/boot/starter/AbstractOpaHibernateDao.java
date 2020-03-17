package com.github.jferrater.opa.data.filter.spring.boot.starter;

import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.to.sql.query.service.OpaClientService;
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

    public List<T> filterData(PartialRequest partialRequest) {
        String sqlStatements = opaClientService.getExecutableSqlStatements(partialRequest);
        NativeQuery<T> nativeQuery = getCurrentSession().createNativeQuery(sqlStatements, clazz);
        return nativeQuery.list();
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
