package com.joffer.opa.ast.to.sql.query.core;

import com.joffer.opa.ast.to.sql.query.core.elements.SqlPredicate;
import com.joffer.opa.ast.to.sql.query.model.response.OpaCompilerResponse;
import com.joffer.opa.ast.to.sql.query.model.response.Predicate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PredicateConverterTest extends TestBase {

    /*
     * Deserialize sample compiler response file from OPA (test classpath: opa-compiler-response.json)
     * and verify translation from OPA Abstract Syntax Tree compiler response to SQL predicate
     */
    @Test
    void shouldConvertTermToSqlQuery() throws IOException {
        OpaCompilerResponse opaCompilerResponse = opaCompilerResponse();
        List<List<Predicate>> queries = opaCompilerResponse.getResult().getQueries();
        Predicate predicate = queries.iterator().next().get(0);

        PredicateConverter target = new PredicateConverter(predicate);
        SqlPredicate result = target.astToSqlPredicate();

        assertThat(result.getOperator(), is("="));
        assertThat(result.getLeftExpression(), is("pets.owner"));
        assertThat(result.getRightExpression(), is("alice"));
    }

}
