package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

/**
 * @author joffryferrater
 *
 * @param <R> The MongoRepository
 * @param <T> The Class type
 * @param <ID> The Class id
 */
public class OpaMongoRepositoryFactoryBean<R extends MongoRepository<T, ID>, T, ID extends Serializable>
        extends MongoRepositoryFactoryBean<R, T, ID> {

    @Autowired
    private MongoQueryService<T> mongoQueryService;

    /**
     * Creates a new {@link MongoRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public OpaMongoRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport getFactoryInstance(MongoOperations operations) {
        return new OpaMongoRepositoryFactory<>(operations, mongoQueryService);
    }
}