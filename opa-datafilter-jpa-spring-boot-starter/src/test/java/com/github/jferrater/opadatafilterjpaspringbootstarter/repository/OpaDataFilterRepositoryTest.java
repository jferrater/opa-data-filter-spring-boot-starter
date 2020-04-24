package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.opadatafilterjpaspringbootstarter.entity.PetEntity;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;
import opa.datafilter.core.ast.db.query.service.DefaultPartialRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

/**
 * @author joffryferrater
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {
                TestApplication.class
        }
)
@ActiveProfiles("test")
public class OpaDataFilterRepositoryTest extends TestBase {


    @Autowired
    private MyService target;

    @MockBean
    RestTemplate opaClient;
    @MockBean
    DefaultPartialRequest defaultPartialRequest;

    private static OpaCompilerResponse opaCompilerResponse;

    @DisplayName("Given a sample Open Policy Agent compiler API response")
    @BeforeAll
    static void init() throws IOException {
        opaCompilerResponse = opaCompilerResponse();
    }

    @Test
    void shouldListFilteredData() throws IOException {
        when(opaClient.postForEntity(anyString(), any(HttpEntity.class), eq(OpaCompilerResponse.class)))
                .thenReturn(new ResponseEntity<>(opaCompilerResponse, HttpStatus.OK));
        ObjectMapper objectMapper = new ObjectMapper();
        PartialRequest partialRequest = objectMapper.readValue(partialRequest(), PartialRequest.class);
        when(defaultPartialRequest.getDefaultPartialRequest()).thenReturn(partialRequest);

        List<PetEntity> results = target.getPets();

        assertThat(results.size(), is(1));
    }

    private String partialRequest() {
        return "{\n" +
                "  \"query\": \"data.petclinic.authz.allow = true\",\n" +
                "  \"input\": {\n" +
                "    \"method\": \"GET\",\n" +
                "    \"path\": [\"pets\"],\n" +
                "    \"subject\": {\n" +
                "      \"user\": \"alice\",\n" +
                "      \"location\": \"SOMA\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"unknowns\": [\"data.pets\"]\n" +
                "}";
    }
}
