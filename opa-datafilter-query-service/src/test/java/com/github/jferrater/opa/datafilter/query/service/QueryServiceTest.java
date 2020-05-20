package com.github.jferrater.opa.datafilter.query.service;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.query.BasicQuery;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author joffryferrater
 */
@ExtendWith(MockitoExtension.class)
class QueryServiceTest {

    private QueryService target;

    @Mock
    private OpaClientService opaClientService;
    @Mock
    private MongoQueryService mongoQueryService;

    @BeforeEach
    void setUp() {
        target = new QueryService(opaClientService, mongoQueryService, new ModelMapper());
    }

    @Test
    void shouldReturnSqlQuery() {
        opa.datafilter.core.ast.db.query.model.request.PartialRequest opaPartialRequest = new opa.datafilter.core.ast.db.query.model.request.PartialRequest();
        when(opaClientService.getExecutableSqlStatements(opaPartialRequest)).thenReturn("SELECT * from pets WHERE pets.name == 'fluffy'");

        QueryResponse result = target.getSqlQuery(new PartialRequest());

        assertThat(result, is(notNullValue()));
    }

    @Test
    void shouldReturnMongoDbQuery() {
        opa.datafilter.core.ast.db.query.model.request.PartialRequest opaPartialRequest = new opa.datafilter.core.ast.db.query.model.request.PartialRequest();
        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, balance : { $gt : 1000.00 }}");
        when(mongoQueryService.getMongoDBQuery(opaPartialRequest)).thenReturn(query);

        QueryResponse result = target.getMongoDbQuery(new PartialRequest());

        assertThat(result, is(notNullValue()));
    }
}