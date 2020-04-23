package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.mongodb;

import com.github.jferrater.opa.ast.db.query.service.OpaClientService;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.mongodb.document.PetDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MappingMongoEntityInformation;

import java.io.Serializable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpaMongoRepositoryFactoryTest {

    @Mock
    MongoOperations operations;
    @Mock@SuppressWarnings("rawtypes")
    OpaClientService opaClientService;
    @Mock @SuppressWarnings("rawtypes")
    MappingContext mappingContext;
    @Mock @SuppressWarnings("rawtypes")
    MongoPersistentEntity entity;
    @Mock
    MongoConverter converter;


    @BeforeEach
    void setUp(){
        when(operations.getConverter()).thenReturn(converter);
        when(converter.getMappingContext()).thenReturn(mappingContext);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void usesMappingMongoEntityInformationIfMappingContextSet() {

        when(mappingContext.getRequiredPersistentEntity(PetDocument.class)).thenReturn(entity);

        OpaMongoRepositoryFactory factory = new OpaMongoRepositoryFactory(operations, opaClientService);
        MongoEntityInformation<PetDocument, Serializable> result = factory.getEntityInformation(PetDocument.class);
        assertThat(result instanceof MappingMongoEntityInformation, is(true));
    }

    @Test
    @SuppressWarnings("rawtypes")
    void createRepositoryWithIdTypeLong() {
        when(mappingContext.getRequiredPersistentEntity(PetDocument.class)).thenReturn(entity);

        OpaMongoRepositoryFactory factory = new OpaMongoRepositoryFactory(operations, opaClientService);

        MyMongoRepository result = factory.getRepository(MyMongoRepository.class);

        assertThat(result, is(notNullValue()));
    }
}
