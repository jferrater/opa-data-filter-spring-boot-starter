package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author joffryferrater
 *
 * @param <T> The class type
 * @param <ID> The class id
 */
@NoRepositoryBean
public class OpaDataFilterMongoRepositoryImpl<T, ID> extends SimpleMongoRepository<T, ID> implements OpaDataFilterMongoRepository<T, ID>{

    private static final Logger LOGGER = LoggerFactory.getLogger(OpaDataFilterMongoRepositoryImpl.class);

    private final MongoQueryService<T> mongoQueryService;
    private final @Nullable MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    /**
     * Creates a new {@link SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link org.springframework.data.mongodb.core.MongoTemplate}.
     *
     * @param metadata        must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     * @param mongoQueryService {@link OpaClientService}
     */
    public OpaDataFilterMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations, MongoQueryService<T> mongoQueryService) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
        this.mongoQueryService = mongoQueryService;
        this.entityInformation = metadata;
    }

    @Override
    public List<T> findAll() {
        LOGGER.trace("Filtering data with OPA, findAll()");
        Query query = mongoQueryService.getMongoDBQuery();
        if (query == null) {
            return Collections.emptyList();
        }
        String collectionName = entityInformation.getCollectionName();
        LOGGER.trace("Collection name: {}", collectionName);
        Class<T> javaType = entityInformation.getJavaType();
        LOGGER.trace("Java type: {}", javaType.getName());
        String queryJson = query.getQueryObject().toJson();
        LOGGER.trace("Query: {}", queryJson);
        LOGGER.trace("MongoOperations: {}", mongoOperations);
        return mongoOperations.find(query, javaType, collectionName);
    }

    @Override
    public List<T> findAll(Sort sort) {
        LOGGER.trace("Filtering data with OPA, findAll(sort={})", sort);
        Query query = mongoQueryService.getMongoDBQuery();
        if (query == null) {
            return Collections.emptyList();
        }
        return mongoOperations.find(query.with(sort), entityInformation.getJavaType(), entityInformation.getCollectionName());
    }
}