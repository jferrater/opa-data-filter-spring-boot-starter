package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.query.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;

/**
 * The repository factory bean
 *
 * @author joffryferrater
 *
 * @param <R> The JpaRepository implementation
 * @param <T> The managed entity
 * @param <ID> The id of the managed entity
 */
public class OpaRepositoryFactoryBean<R extends JpaRepository<T, ID>, T, ID> extends JpaRepositoryFactoryBean<R, T, ID> {

    @Autowired
    private QueryService<T> queryService;

    /**
     * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public OpaRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new OpaRepositoryFactory<>(entityManager, queryService);
    }

}
