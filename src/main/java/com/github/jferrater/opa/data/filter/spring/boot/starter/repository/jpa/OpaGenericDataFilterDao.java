package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 *
 * @author joffryferrater
 *
 * The generic dao class for filtering data using OPA
 *
 * @param <T> The managed entity
 */
@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OpaGenericDataFilterDao<T> extends AbstractOpaJpaDao<T> {

}
