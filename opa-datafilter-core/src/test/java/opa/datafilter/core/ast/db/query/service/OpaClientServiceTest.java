package opa.datafilter.core.ast.db.query.service;

import opa.datafilter.core.ast.db.query.TestBase;
import opa.datafilter.core.ast.db.query.config.OpaConfig;
import opa.datafilter.core.ast.db.query.exception.OpaClientException;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.sql.PetEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

/**
 * @author joffryferrater
 */
class OpaClientServiceTest extends TestBase {

    private RestTemplate restTemplate;
    private OpaCompilerResponse opaCompilerResponse;
    private OpaClientService<PetEntity> target;

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

    @Test
    void shouldGetOpaCompileApiResponse() {
        when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(OpaCompilerResponse.class)))
                .thenReturn(new ResponseEntity<>(opaCompilerResponse, HttpStatus.OK));
        PartialRequest partialRequest = PartialRequest.builder()
                .query("data.petclinic.authz.allow = true")
                .unknowns(Set.of("data.pets")).build();


        OpaCompilerResponse result = target.getOpaCompilerApiResponse(partialRequest);

        assertThat(result, is(notNullValue()));
    }
}