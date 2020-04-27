package com.github.jferrater.opadatafiltermongospringbootstarter.query;

import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.model.response.Predicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.IOException;
import java.util.List;

import static com.github.jferrater.opadatafiltermongospringbootstarter.TestBase.opaCompilerResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
class AstToMongoDBQueryTest {

    private AstToMongoDBQuery target;

    private static OpaCompilerResponse opaCompilerResponse;

    @DisplayName("Given a sample Open Policy Agent compiler API response")
    @BeforeAll
    static void init() throws IOException {
        opaCompilerResponse = opaCompilerResponse();
    }

    @BeforeEach
    void setUp() {
        target = new AstToMongoDBQuery(opaCompilerResponse);
    }

    @Test
    void shouldConvertSqlPredicateEqualOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "alice");

        Criteria result = target.initialCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"owner\": \"alice\"}"));
    }

    @Test
    void shouldConvertSqlPredicateLessThanOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<", "3");

        Criteria result = target.initialCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"owner\": {\"$lt\": 3}}"));
    }

    @Test
    void shouldConvertSqlPredicateGreaterThanOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">", "4");

        Criteria result = target.initialCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"owner\": {\"$gt\": 4}}"));
    }

    @Test
    void shouldNotChainToOrOperatorForSingleCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">", "4");
        Criteria criteria = target.initialCriteria(sqlPredicate1);

        Criteria result = target.chainCriteriaByOrOperator(List.of(criteria));
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"owner\": {\"$gt\": 4}}"));
    }

    @Test
    void shouldChainCriteriaByAndOperationCriteria() {
        List<Predicate> predicates = opaCompilerResponse.getResult().getQueries().get(0);

        Criteria result = target.chainCriteriaByAndOperator(predicates);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"owner\": \"alice\", \"name\": \"fluffy\"}"));
    }

    @Test
    void shouldCreateMongoDBQuery() {
        Query query = target.createQuery();

        assertThat(query, is(notNullValue()));

        String result = query.getQueryObject().toJson();
        assertThat(result, is("{\"$or\": [{\"owner\": \"alice\", \"name\": \"fluffy\"}, {\"veterinarian\": \"alice\", \"clinic\": \"SOMA\", \"name\": \"fluffy\"}]}"));
    }
}