package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

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

        RepositoryFactorySupport object = factoryBean.getFactoryInstance(operations);
        assertThat(object instanceof OpaMongoRepositoryFactory, is(true));
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void doesNotAddIndexEnsuringQueryCreationListenerByDefault() {

        List<Object> listeners = getListenersFromFactory(new OpaMongoRepositoryFactoryBean<>(MyMongoRepository.class));
        assertThat(listeners.size(), is(1));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List<Object> getListenersFromFactory(OpaMongoRepositoryFactoryBean factoryBean) {
        factoryBean.setLazyInit(true);
        factoryBean.setMongoOperations(operations);
        factoryBean.afterPropertiesSet();

        RepositoryFactorySupport factory = factoryBean.getFactoryInstance(operations);
        return (List<Object>) ReflectionTestUtils.getField(factory, "queryPostProcessors");
    }
}