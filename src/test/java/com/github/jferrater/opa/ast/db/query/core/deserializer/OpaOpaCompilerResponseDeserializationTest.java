package com.github.jferrater.opa.ast.db.query.core.deserializer;

import com.github.jferrater.opa.ast.db.query.model.response.OpaCompilerResponse;
import com.github.jferrater.opa.ast.db.query.model.response.Predicate;
import com.github.jferrater.opa.ast.db.query.model.response.Term;
import com.github.jferrater.opa.ast.db.query.model.response.Value;
import com.github.jferrater.opa.ast.db.query.core.TestBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OpaOpaCompilerResponseDeserializationTest extends TestBase {

    /*
     * Deserialize sample compiler response file from OPA (test classpath: opa-compiler-response.json)
     */
    @Test
    void shouldDeserializeOpaResponse() throws IOException {
        OpaCompilerResponse opaCompilerResponse = opaCompilerResponse();

        List<List<Predicate>> queries = opaCompilerResponse.getResult().getQueries();
        assertThat(queries.size(), is(2));

        List<Predicate> predicateList1 = queries.get(0);
        assertThat(predicateList1.size(), is(2));

        Predicate predicate1 = predicateList1.get(0);
        List<Term> terms = predicate1.getTerms();
        Term term1 = terms.get(0);
        assertThat(term1.getType(), is("ref"));
        Object term1Value = term1.getValue();
        assertThat(term1Value instanceof ArrayList, is(true));
        List<Value> valueList1 = (List<Value>) term1Value;
        Value value1 = valueList1.get(0);
        assertThat(value1.getType(), is("var"));
        assertThat(value1.getValues(), is("eq"));

        Term term2 = terms.get(1);
        assertThat(term2.getType(), is("string"));
        assertThat(term2.getValue() instanceof String, is(true));
        assertThat(term2.getValue(), is("alice"));

        List<Predicate> predicateList2 = queries.get(1);
        assertThat(predicateList2.size(), is(3));
    }

}
