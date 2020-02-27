package com.joffryferrater.opadatafilterspringbootstarter.service;

import com.joffryferrater.opadatafilterspringbootstarter.config.OpaConfig;
import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.mockito.Mockito.mock;

class OpaClientServiceTest {

    private RestTemplate restTemplate;
    private OpaClientService target;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        OpaConfig opaConfig = new OpaConfig();
        opaConfig.setDataFilterEnabled(true);
        opaConfig.setUrl("http://localhost:8181");
        target = new OpaClientService(opaConfig, restTemplate);
    }

    @Test
    void shouldGetTheSqlStatementsFromPartialRequest() {
        PartialRequest partialRequest = new PartialRequest();
        partialRequest.setQuery("data.petclinic.authz.allow = true");
        partialRequest.setUnknowns(Set.of("data.pets"));

        String result = target.getExecutableSqlStatements(partialRequest);
    }
}