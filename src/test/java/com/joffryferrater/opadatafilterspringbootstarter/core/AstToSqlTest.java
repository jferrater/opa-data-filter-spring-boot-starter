package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Predicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class AstToSqlTest extends TestBase {

    private AstToSql target;
    private static OpaCompilerResponse opaCompilerResponse;

    @DisplayName("Given a sample Open Policy Agent compiler API response")
    @BeforeAll
    static void init() throws IOException {
        opaCompilerResponse = opaCompilerResponse();
    }

    @BeforeEach
    void setUp() {
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setUnknowns(Set.of("data.pets"));
        target = new AstToSql(partialRequest, opaCompilerResponse);
    }

    @Test
    void shouldGetConstraintsFromPredicates() {
        List<Predicate> predicates = opaCompilerResponse.getResult().getQueries().get(0);

        String result = target.andOperationConstraints(predicates);

        assertThat(result, is("(pets.owner = 'alice' AND pets.name = 'fluffy')"));
    }

    @Test
    void shouldGetSqlQueryStatements() {
        String result = target.getSqlQueryStatements();
        assertThat(result, is("SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');"));
    }
}