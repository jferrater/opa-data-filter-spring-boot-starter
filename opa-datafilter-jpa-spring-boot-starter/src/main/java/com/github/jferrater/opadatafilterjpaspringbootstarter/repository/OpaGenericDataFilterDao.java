package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * The generic dao class for filtering data using OPA
 *
 * @author joffryferrater
 *
 * @param <T> The managed entity
 */
@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OpaGenericDataFilterDao<T> extends AbstractOpaJpaDao<T> {

}
