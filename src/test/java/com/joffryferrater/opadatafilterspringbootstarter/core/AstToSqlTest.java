package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class AstToSqlTest extends TestBase {

    private AstToSql target;

    @BeforeEach
    void setUp() {
        target = new AstToSql();
    }

    @Test
    void shouldGetConstraintsFromPredicates() throws IOException {
        OpaCompilerResponse opaCompilerResponse = opaCompilerResponse();
        List<Predicate> predicates = opaCompilerResponse.getResult().getQueries().get(0);

        String result = target.andConstraints(predicates);

        assertThat(result, is("(pets.owner = 'alice' AND pets.name = 'fluffy')"));
    }
}