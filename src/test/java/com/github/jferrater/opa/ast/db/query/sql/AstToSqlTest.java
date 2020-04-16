package com.github.jferrater.opa.ast.db.query.sql;

import com.github.jferrater.opa.ast.db.query.core.TestBase;
import com.github.jferrater.opa.ast.db.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.db.query.model.response.OpaCompilerResponse;
import com.github.jferrater.opa.ast.db.query.model.response.Predicate;
import com.github.jferrater.opa.ast.db.query.sql.AstToSql;
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
        partialRequest = PartialRequest.builder()
                .unknowns(Set.of("data.pets")).build();
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