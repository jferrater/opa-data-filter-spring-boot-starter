package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpaMongoRepositoryFactoryBeanTest {

    @Mock
    MongoOperations operations;
    @Mock
    MongoConverter converter;
    @Mock @SuppressWarnings("rawtypes")
    MappingContext context;

    @BeforeEach
    void setUp() {
        when(operations.getConverter()).thenReturn(converter);
        when(converter.getMappingContext()).thenReturn(context);
    }

    @Test
    @SuppressWarnings("rawtypes")
    void shouldReturnInstanceOfOpaDataFilterMongoRepository() {
        OpaMongoRepositoryFactoryBean factoryBean = new OpaMongoRepositoryFactoryBean(MyMongoRepository.class);
        factoryBean.setMongoOperations(operations);
        factoryBean.setLazyInit(true);
        factoryBean.setCreateIndexesForQueryMethods(true);

        RepositoryFactorySupport object = factoryBean.createRepositoryFactory();
        assertThat(object instanceof OpaMongoRepositoryFactory, is(true));
    }
}