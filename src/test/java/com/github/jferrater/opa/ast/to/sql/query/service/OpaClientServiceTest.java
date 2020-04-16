package com.github.jferrater.opa.ast.to.sql.query.service;

import com.github.jferrater.opa.ast.to.sql.query.core.TestBase;
import com.github.jferrater.opa.ast.to.sql.query.exception.OpaClientException;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.to.sql.query.config.OpaConfig;
import com.github.jferrater.opa.ast.to.sql.query.model.response.OpaCompilerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpaClientServiceTest extends TestBase {

    private RestTemplate restTemplate;
    private OpaCompilerResponse opaCompilerResponse;
    private OpaClientService target;

    @BeforeEach
    void setUp() throws IOException {
        restTemplate = mock(RestTemplate.class);
        opaCompilerResponse = opaCompilerResponse();
        OpaConfig opaConfig = new OpaConfig();
        opaConfig.setDataFilterEnabled(true);
        opaConfig.setUrl("http://localhost:8181/v1/compile");
        target = new OpaClientService(opaConfig, restTemplate);
    }

    @Test
    void shouldGetTheSqlStatementsFromPartialRequest() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(OpaCompilerResponse.class)))
                .thenReturn(new ResponseEntity<>(opaCompilerResponse, HttpStatus.OK));
        PartialRequest partialRequest = PartialRequest.builder()
                .query("data.petclinic.authz.allow = true")
                .unknowns(Set.of("data.pets")).build();
                

        String result = target.getExecutableSqlStatements(partialRequest);

        assertThat(result, is(notNullValue()));
        assertThat(result, is("SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');"));
    }

    @Test
    void shouldGetMongoDbQueryFromOpaCompilerResponse() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(OpaCompilerResponse.class)))
                .thenReturn(new ResponseEntity<>(opaCompilerResponse, HttpStatus.OK));
        PartialRequest partialRequest = PartialRequest.builder()
                .query("data.petclinic.authz.allow = true")
                .unknowns(Set.of("data.pets")).build();


        Query result = target.getMongoDBQuery(partialRequest);

        assertThat(result, is(notNullValue()));
        String resultInString = result.getQueryObject().toJson();
        assertThat(resultInString, is("{\"$or\": [{\"pets.owner\": \"alice\", \"pets.name\": \"fluffy\"}, {\"pets.veterinarian\": \"alice\", \"pets.clinic\": \"SOMA\", \"pets.name\": \"fluffy\"}]}"));
    }

    @Test
    void shouldThrowOpaClientException() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(OpaCompilerResponse.class)))
                .thenReturn(new ResponseEntity<>(opaCompilerResponse, HttpStatus.BAD_REQUEST));
        PartialRequest partialRequest = PartialRequest.builder()
                .query("data.petclinic.authz.allow = true")
                .unknowns(Set.of("data.pets")).build();

        assertThrows(OpaClientException.class, () -> {
            target.getExecutableSqlStatements(partialRequest);
        });
    }
}