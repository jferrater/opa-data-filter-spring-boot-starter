package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.query.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author joffryferrater
 *
 * Overrides the {{@link #findAll()}} method of the {@link SimpleJpaRepository} to enforce authorization
 * with the Partial Evaluation of the Open Policy Agent
 *
 * @param <T> The managed entity
 * @param <ID> The id of the managed entity
 */
@NoRepositoryBean
public class OpaDataFilterRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements OpaDataFilterRepository<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpaDataFilterRepositoryImpl.class);

    private final QueryService<T> queryService;
    private final EntityManager entityManager;

    public OpaDataFilterRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager, QueryService<T> queryService) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.queryService = queryService;
    }

    public OpaDataFilterRepositoryImpl(Class<T> domainClass, EntityManager em, QueryService<T> queryService) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em, queryService);
    }

    @Override
    public List<T> findAll() {
        LOGGER.trace("Entering findAll()");
        TypedQuery<T> typedQuery = queryService.getTypedQuery(this.getDomainClass(), Sort.unsorted(), entityManager);
        List<T> resultList = typedQuery.getResultList();
        LOGGER.trace("Result list size: {}", resultList.size());
        return resultList;
    }

    @Override
    public List<T> findAll(Sort sort) {
        LOGGER.trace("Entering findAll(sort={})", sort);
        TypedQuery<T> typedQuery = queryService.getTypedQuery(this.getDomainClass(), sort, entityManager);
        List<T> resultList = typedQuery.getResultList();
        LOGGER.trace("Result list size: {}", resultList.size());
        return resultList;
    }
}
