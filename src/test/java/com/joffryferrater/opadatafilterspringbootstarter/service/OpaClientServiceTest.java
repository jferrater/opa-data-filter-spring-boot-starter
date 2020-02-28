package com.joffryferrater.opadatafilterspringbootstarter.service;

import com.joffryferrater.opadatafilterspringbootstarter.config.OpaConfig;
import com.joffryferrater.opadatafilterspringbootstarter.core.TestBase;
import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;
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
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setQuery("data.petclinic.authz.allow = true");
        partialRequest.setUnknowns(Set.of("data.pets"));

        String result = target.getExecutableSqlStatements(partialRequest);

        assertThat(result, is(notNullValue()));
        assertThat(result, is("SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');"));
    }
}