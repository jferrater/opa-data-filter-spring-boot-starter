package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.mongodb;

import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final OpaClientService<T> opaClientService;
    private final @Nullable MongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;

    /**
     * Creates a new {@link SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link org.springframework.data.mongodb.core.MongoTemplate}.
     *
     * @param metadata        must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     * @param opaClientService {@link OpaClientService}
     */
    public OpaDataFilterMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations, OpaClientService<T> opaClientService) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
        this.opaClientService = opaClientService;
        this.entityInformation = metadata;
    }

    @Override
    public List<T> findAll() {
        LOGGER.info("Filtering data with OPA, findAll()");
        Query query = opaClientService.getMongoDBQuery();
        if (query == null) {
            return Collections.emptyList();
        }
        return mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
    }
}
