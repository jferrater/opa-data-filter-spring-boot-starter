package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.mongodb;

import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * @author joffryferrater
 *
 * @param <T> The class type
 * @param <ID> The class id
 */
public class OpaMongoRepositoryFactory<T, ID> extends MongoRepositoryFactory {

    private final OpaClientService<T> opaClientService;
    private final @Nullable MongoOperations mongoOperations;
    private final MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext;

    /**
     * Creates a new {@link MongoRepositoryFactory} with the given {@link MongoOperations}.
     *
     * @param mongoOperations must not be {@literal null}.
     * @param opaClientService {@link OpaClientService}
     */
    public OpaMongoRepositoryFactory(MongoOperations mongoOperations, OpaClientService<T> opaClientService) {
        super(mongoOperations);
        this.mongoOperations = mongoOperations;
        this.mappingContext = mongoOperations.getConverter().getMappingContext();
        this.opaClientService = opaClientService;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation information) {
        MongoEntityInformation<T, ID> entityInformation = getEntityInformation((Class<T>)information.getDomainType(), information);
        return new OpaDataFilterMongoRepositoryImpl<>(entityInformation, mongoOperations, opaClientService);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return OpaDataFilterMongoRepository.class;
    }

    private MongoEntityInformation<T, ID> getEntityInformation(Class<T> domainClass, @Nullable RepositoryMetadata metadata) {
        MongoPersistentEntity<?> entity = mappingContext.getRequiredPersistentEntity(domainClass);
        Assert.notNull(entity, "Entity must not be null!");
        Class<ID> classIdType = metadata != null ? (Class<ID>)metadata.getIdType() : null;
        return new MappingMongoEntityInformation<>((MongoPersistentEntity<T>)entity, classIdType);
    }
}
