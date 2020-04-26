package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.Nullable;

import javax.annotation.Resource;
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

    private MongoOperations operations;
    @Resource(name = "opaClientService")
    private OpaClientService<T> opaClientService;

    /**
     * Creates a new {@link MongoRepositoryFactoryBean} for the given repository interface.
     *
     * @param repositoryInterface must not be {@literal null}.
     */
    public OpaMongoRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void setMongoOperations(MongoOperations operations) {
        this.operations = operations;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new OpaMongoRepositoryFactory<>(operations, opaClientService);
    }
}