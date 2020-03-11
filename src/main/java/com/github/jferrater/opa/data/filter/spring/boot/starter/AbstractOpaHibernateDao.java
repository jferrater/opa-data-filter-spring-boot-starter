package com.github.jferrater.opa.data.filter.spring.boot.starter;

import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;

import java.util.List;

public abstract class AbstractOpaHibernateDao<T> {

    private Class<T> clazz;

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> filterData(PartialRequest partialRequest) {
        return null;
    }
}
