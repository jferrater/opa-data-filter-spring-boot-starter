package opa.datafilter.core.ast.db.query.util;

import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
class SqlUtilTest {

    @Test
    void shouldConcatenateSetOfStrings() {
        List<String> strings = List.of("name", "address");
        String result = SqlUtil.concat(strings, ", ");
        assertThat(result, is("name, address"));
    }

    @Test
    void shouldReturnNullIfListIsEmpty(){
        String result = SqlUtil.concat(Collections.emptyList(), ", ");
        assertThat(result, is(nullValue()));
    }

    @Test
    void shouldConcatenateAndConstraints() {
        SqlPredicate sqlPredicate1 = new SqlPredicate("pets.owner", "=", "alice");
        SqlPredicate sqlPredicate2 = new SqlPredicate("pets.name", "=", "fluffy");
        List<SqlPredicate> predicates = List.of(sqlPredicate1, sqlPredicate2);

        String result = SqlUtil.concatByAndOperation(predicates);

        assertThat(result, is("(pets.owner = 'alice' AND pets.name = 'fluffy')"));
    }

    @Test
    void shouldReturnNullIfListIsEmptyAndOPeration(){
        String result = SqlUtil.concatByAndOperation(Collections.emptyList());
        assertThat(result, is(nullValue()));
    }

    @Test
    void shouldGetTheTableName() {
        String opaUnknownPropertyValue = "data.pets";
        assertThat(SqlUtil.getTableName(opaUnknownPropertyValue), is("pets"));
    }
}