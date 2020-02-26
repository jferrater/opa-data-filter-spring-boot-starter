package com.joffryferrater.opadatafilterspringbootstarter.core.util;

import com.joffryferrater.opadatafilterspringbootstarter.core.elements.SqlPredicate;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class SqlUtilTest {

    @Test
    void shouldConcatenateSetOfStrings() {
        List<String> strings = List.of("name", "address");
        String result = SqlUtil.concat(strings, ", ");
        assertThat(result, is("name, address"));
    }

    @Test
    void shouldConcatenateAndConstraints() {
        SqlPredicate sqlPredicate1 = new SqlPredicate();
        sqlPredicate1.setOperator("=");
        sqlPredicate1.setLeftExpression("pets.owner");
        sqlPredicate1.setRightExpression("alice");
        SqlPredicate sqlPredicate2 = new SqlPredicate();
        sqlPredicate2.setOperator("=");
        sqlPredicate2.setLeftExpression("pets.name");
        sqlPredicate2.setRightExpression("fluffy");
        List<SqlPredicate> predicates = List.of(sqlPredicate1, sqlPredicate2);

        String result = SqlUtil.concatByAndOperation(predicates);

        assertThat(result, is("(pets.owner = 'alice' AND pets.name = 'fluffy')"));
    }
}