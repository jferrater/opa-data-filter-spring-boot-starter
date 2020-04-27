package com.github.jferrater.opadatafiltermongospringbootstarter.query;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.document.PetDocument;
import opa.datafilter.core.ast.db.query.config.OpaConfig;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Set;

import static com.github.jferrater.opadatafiltermongospringbootstarter.TestBase.opaCompilerResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MongoQueryServiceTest {

    private RestTemplate restTemplate;
    private OpaCompilerResponse opaCompilerResponse;
    private OpaClientService opaClientService;

    private MongoQueryService<PetDocument> target;

    @BeforeEach
    void setUp() throws IOException {
        restTemplate = mock(RestTemplate.class);
        opaCompilerResponse = opaCompilerResponse();
        OpaConfig opaConfig = new OpaConfig();
        opaConfig.setDataFilterEnabled(true);
        opaConfig.setUrl("http://localhost:8181/v1/compile");
        opaClientService = new OpaClientService(opaConfig, restTemplate);
        target = new MongoQueryService<>(opaClientService);
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
        assertThat(resultInString, is("{\"$or\": [{\"owner\": \"alice\", \"name\": \"fluffy\"}, {\"veterinarian\": \"alice\", \"clinic\": \"SOMA\", \"name\": \"fluffy\"}]}"));
    }
}