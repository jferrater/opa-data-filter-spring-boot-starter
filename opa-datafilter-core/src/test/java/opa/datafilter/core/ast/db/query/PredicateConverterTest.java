package opa.datafilter.core.ast.db.query;

import opa.datafilter.core.ast.db.query.elements.SqlPredicate;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.model.response.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static opa.datafilter.core.ast.db.query.model.OpaConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author joffryferrater
 */
class PredicateConverterTest extends TestBase {

    private PredicateConverter target;

    @BeforeEach
    void setUp() throws IOException {
        OpaCompilerResponse opaCompilerResponse = opaCompilerResponse();
        List<List<Predicate>> queries = opaCompilerResponse.getResult().getQueries();
        Predicate predicate = queries.iterator().next().get(0);
        target = new PredicateConverter(predicate);
    }
    /*
     * Deserialize sample compiler response file from OPA (test classpath: opa-compiler-response.json)
     * and verify translation from OPA Abstract Syntax Tree compiler response to SQL predicate
     */
    @Test
    void shouldConvertTermToSqlQuery() {

        SqlPredicate result = target.astToSqlPredicate();

        assertThat(result.getOperator(), is("="));
        assertThat(result.getLeftExpression(), is("pets.owner"));
        assertThat(result.getRightExpression(), is("alice"));
    }

    @Test
    void shouldTestBooleanOperator() {
        assertThat(target.getOperatorFromValue(EQ), is("="));
        assertThat(target.getOperatorFromValue(LT), is("<"));
        assertThat(target.getOperatorFromValue(LTE), is("<="));
        assertThat(target.getOperatorFromValue(GT), is(">"));
        assertThat(target.getOperatorFromValue(GTE), is(">="));
    }
}
