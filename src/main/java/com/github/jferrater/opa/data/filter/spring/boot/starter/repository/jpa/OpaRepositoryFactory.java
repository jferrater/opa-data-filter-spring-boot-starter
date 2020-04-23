package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;

import javax.persistence.EntityManager;

/**
 *
 * @author joffryferrater
 *
 * The custom repository factory
 *
 * @param <T> The managed entity
 */
public class OpaRepositoryFactory<T, ID> extends JpaRepositoryFactory {


    private final EntityManager entityManager;
    private final OpaClientService<T> opaClientService;

    /**
     * Creates a new {@link JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     * @param opaClientService {@link OpaClientService}
     */
    public OpaRepositoryFactory(EntityManager entityManager, OpaClientService<T> opaClientService) {
        super(entityManager);
        this.entityManager = entityManager;
        this.opaClientService = opaClientService;
    }

    @Override
    protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
        return new OpaDataFilterRepositoryImpl<>((Class<T>)information.getDomainType(), this.entityManager, opaClientService);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return OpaDataFilterRepository.class;
    }
}
