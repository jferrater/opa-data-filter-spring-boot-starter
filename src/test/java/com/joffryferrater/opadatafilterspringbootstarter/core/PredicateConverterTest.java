package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joffryferrater.opadatafilterspringbootstarter.core.elements.SqlPredicate;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Predicate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class PredicateConverterTest {

    /*
     * Deserialize sample compiler response file from OPA (test classpath: opa-compiler-response.json)
     * and verify translation from OPA Abstract Syntax Tree compiler response to SQL predicate
     */
    @Test
    void shouldConvertTermToSqlQuery() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OpaCompilerResponse opaCompilerResponse = objectMapper.readValue(response(), OpaCompilerResponse.class);
        List<List<Predicate>> queries = opaCompilerResponse.getResult().getQueries();
        Predicate predicate = queries.iterator().next().get(0);

        PredicateConverter target = new PredicateConverter(predicate);
        SqlPredicate result = target.astToSqlPredicate();

        assertThat(result.getOperator(), is("="));
        assertThat(result.getLeftExpression(), is("pets.owner"));
        assertThat(result.getRightExpression(), is("alice"));
    }

    private String response() throws IOException {
        return Files.readString(Paths.get("src/test/resources/opa-compiler-response.json"));
    }

}
