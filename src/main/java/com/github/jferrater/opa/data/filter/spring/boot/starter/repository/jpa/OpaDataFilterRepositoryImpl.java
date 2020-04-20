package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
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

@NoRepositoryBean
public class OpaDataFilterRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements OpaDataFilterRepository<T, ID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpaDataFilterRepositoryImpl.class);

    private final OpaClientService<T> opaClientService;
    private final EntityManager entityManager;

    public OpaDataFilterRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager, OpaClientService<T> opaClientService) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.opaClientService = opaClientService;
    }

    public OpaDataFilterRepositoryImpl(Class<T> domainClass, EntityManager em, OpaClientService<T> opaClientService) {
        this(JpaEntityInformationSupport.getEntityInformation(domainClass, em), em, opaClientService);
        LOGGER.info("here second constructor");
    }

    @Override
    public List<T> findAll() {
        LOGGER.trace("Entering findAll()");
        LOGGER.trace("ClassName: {}", this.getDomainClass().getName());
        TypedQuery<T> typedQuery = opaClientService.getTypedQuery(this.getDomainClass(), Sort.unsorted(), entityManager);
        List<T> resultList = typedQuery.getResultList();
        LOGGER.trace("Result list size: {}", resultList.size());
        return resultList;
    }
}
