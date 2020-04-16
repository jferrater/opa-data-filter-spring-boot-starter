package com.github.jferrater.opa.ast.to.sql.query.mongodb;

import com.github.jferrater.opa.ast.to.sql.query.core.elements.SqlPredicate;
import com.github.jferrater.opa.ast.to.sql.query.model.response.OpaCompilerResponse;
import com.github.jferrater.opa.ast.to.sql.query.model.response.Predicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.io.IOException;
import java.util.List;

import static com.github.jferrater.opa.ast.to.sql.query.core.TestBase.opaCompilerResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class MongoDbQueryBuilderTest {

    private MongoDbQueryBuilder target;

    private static OpaCompilerResponse opaCompilerResponse;

    @DisplayName("Given a sample Open Policy Agent compiler API response")
    @BeforeAll
    static void init() throws IOException {
        opaCompilerResponse = opaCompilerResponse();
    }

    @BeforeEach
    void setUp() {
        target = new MongoDbQueryBuilder(opaCompilerResponse);
    }

    @Test
    void shouldConvertSqlPredicateEqualOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "alice");

        Criteria result = target.initialCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": \"alice\"}"));
    }

    @Test
    void shouldConvertSqlPredicateLessThanOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<", "alice");

        Criteria result = target.initialCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": {\"$lt\": \"alice\"}}"));
    }

    @Test
    void shouldConvertSqlPredicateGreaterThanOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">", "alice");

        Criteria result = target.initialCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": {\"$gt\": \"alice\"}}"));
    }

    @Test
    void shouldBuildAndOperationCriteria() {
        List<Predicate> predicates = opaCompilerResponse.getResult().getQueries().get(0);

        Criteria result = target.chainCriterias(predicates);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": \"alice\", \"pets.name\": \"fluffy\"}"));
    }
}