package com.joffryferrater.opadatafilterspringbootstarter.core.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Query;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Term;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Value;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OpaOpaCompilerResponseDeserializationTest {

    /*
     * Deserialize sample compiler response file from OPA (test classpath: opa-compiler-response.json)
     */
    @Test
    void shouldDeserializeOpaResponse() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OpaCompilerResponse opaCompilerResponse = objectMapper.readValue(opaCompilerResponse(), OpaCompilerResponse.class);

        List<List<Query>> queries = opaCompilerResponse.getResult().getQueries();
        assertThat(queries.size(), is(2));

        List<Query> queryList1 = queries.get(0);
        assertThat(queryList1.size(), is(2));

        Query query1 = queryList1.get(0);
        List<Term> terms = query1.getTerms();
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

        List<Query> queryList2 = queries.get(1);
        assertThat(queryList2.size(), is(3));
    }

    private String opaCompilerResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/opa-compiler-response.json"));
    }

}
