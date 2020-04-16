package com.github.jferrater.opa.ast.to.sql.query.mongodb;

import com.github.jferrater.opa.ast.to.sql.query.core.elements.SqlPredicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class MongoDbQueryBuilderTest {

    private MongoDbQueryBuilder target;

    @BeforeEach
    void setUp() {
        target = new MongoDbQueryBuilder();
    }

    @Test
    void shouldConvertSqlPredicateEqualOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "alice");

        Criteria result = target.whereCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": \"alice\"}"));
    }

    @Test
    void shouldConvertSqlPredicateLessThanOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "<", "alice");

        Criteria result = target.whereCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": {\"$lt\": \"alice\"}}"));
    }

    @Test
    void shouldConvertSqlPredicateGreaterThanOperatorToMongoDbCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", ">", "alice");

        Criteria result = target.whereCriteria(sqlPredicate1);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": {\"$gt\": \"alice\"}}"));
    }

    @Test
    void shouldBuildAndOperationCriteria() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "dodong");
        SqlPredicate sqlPredicate2 = new SqlPredicate("pets.veterinarian", "=", "alice");
        List<SqlPredicate> predicates = List.of(sqlPredicate1, sqlPredicate2);

        Criteria result = target.buildAndCriteriaFromSqlPredicates(predicates);
        String resultInString = result.getCriteriaObject().toJson();

        assertThat(resultInString, is("{\"pets.owner\": \"dodong\", \"pets.veterinarian\": \"alice\"}"));
    }
}