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
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "alice");
        SqlPredicate sqlPredicate2 = new SqlPredicate("pets.name", "=", "fluffy");
        List<SqlPredicate> predicates = List.of(sqlPredicate1, sqlPredicate2);

        String result = SqlUtil.concatByAndOperation(predicates);

        assertThat(result, is("(pets.owner = 'alice' AND pets.name = 'fluffy')"));
    }
}