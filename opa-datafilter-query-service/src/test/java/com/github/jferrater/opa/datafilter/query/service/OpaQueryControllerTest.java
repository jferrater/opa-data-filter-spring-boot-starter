package com.github.jferrater.opa.datafilter.query.service;

import com.github.jferrater.opadatafiltermongospringbootstarter.query.MongoQueryService;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author joffryferrater
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class OpaQueryControllerTest {

    private static final String EXPECTED_SQL_QUERY = "SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OpaClientService opaClientService;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private MongoQueryService mongoQueryService;

    @Test
    void shouldQueryResponseWithSqlQuey() throws Exception {
        PartialRequest partialRequest = new PartialRequest();
        when(modelMapper.map(any(com.github.jferrater.opa.datafilter.query.service.PartialRequest.class), eq(PartialRequest.class))).thenReturn(partialRequest);
        when(opaClientService.getExecutableSqlStatements(any(PartialRequest.class))).thenReturn(EXPECTED_SQL_QUERY);

        mockMvc.perform(post("/query?type=sql")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(EXPECTED_SQL_QUERY)));
    }

    @Test
    void shouldQueryResponseWithMongoDbQuey() throws Exception {
        PartialRequest partialRequest = new PartialRequest();
        when(modelMapper.map(any(com.github.jferrater.opa.datafilter.query.service.PartialRequest.class), eq(PartialRequest.class))).thenReturn(partialRequest);
        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, balance : { $gt : 1000.00 }}");
        when(mongoQueryService.getMongoDBQuery(any(PartialRequest.class))).thenReturn(query);

        mockMvc.perform(post("/query?type=mongodb")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result", is(query.getQueryObject().toJson())));
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/query?type=notValid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody()))
                .andExpect(status().isBadRequest());
    }

    private String requestBody() {
        return "{\n" +
                "  \"query\": \"data.petclinic.authz.allow = true\",\n" +
                "  \"input\": {\n" +
                "    \"method\": \"GET\",\n" +
                "    \"path\": [\"pets\", \"fluffy\"],\n" +
                "    \"subject\": {\n" +
                "      \"user\": \"alice\",\n" +
                "      \"location\": \"SOMA\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"unknowns\": [\"data.pets\"]\n" +
                "}";
    }
}