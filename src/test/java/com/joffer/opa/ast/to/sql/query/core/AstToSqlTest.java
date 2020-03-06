package com.joffer.opa.ast.to.sql.query.core;

import com.joffer.opa.ast.to.sql.query.model.request.PartialRequest;
import com.joffer.opa.ast.to.sql.query.model.response.OpaCompilerResponse;
import com.joffer.opa.ast.to.sql.query.model.response.Predicate;
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
    private PartialRequest partialRequest;

    @DisplayName("Given a sample Open Policy Agent compiler API response")
    @BeforeAll
    static void init() throws IOException {
        opaCompilerResponse = opaCompilerResponse();
    }

    @BeforeEach
    void setUp() {
        partialRequest = new PartialRequest();
        partialRequest.setUnknowns(Set.of("data.pets"));
        target = new AstToSql(opaCompilerResponse);
    }

    @Test
    void shouldGetConstraintsFromPredicates() {
        List<Predicate> predicates = opaCompilerResponse.getResult().getQueries().get(0);

        String result = target.andOperationConstraints(predicates);

        assertThat(result, is("(pets.owner = 'alice' AND pets.name = 'fluffy')"));
    }

    @Test
    void shouldGetSqlQueryStatementsFromPartialRequests() {
        String result = target.getSqlQueryStatements(partialRequest);
        assertThat(result, is("SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');"));
    }

    @Test
    void shouldGetSqlQueryStatementsFromUnknowns() {
        String result = target.getSqlQueryStatements(Set.of("data.pets"));
        assertThat(result, is("SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');"));
    }
}