package com.github.jferrater.opa.data.filter.spring.boot.starter;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OpaGenericDataFilterDao<T> extends AbstractOpaHibernateDao<T> {

}
