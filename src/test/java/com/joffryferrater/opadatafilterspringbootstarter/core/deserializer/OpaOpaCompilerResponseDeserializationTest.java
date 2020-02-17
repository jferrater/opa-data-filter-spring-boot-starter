package com.joffryferrater.opadatafilterspringbootstarter.core.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.Query;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OpaOpaCompilerResponseDeserializationTest {

    @Test
    void shouldDeserializeOpaResponse() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        OpaCompilerResponse opaCompilerResponse = objectMapper.readValue(response(), OpaCompilerResponse.class);

        List<List<Query>> queries = opaCompilerResponse.getResult().getQueries();
        assertThat(queries.size(), is(2));

        List<Query> queryList1 = queries.get(0);
        assertThat(queryList1.size(), is(2));
        List<Query> queryList2 = queries.get(1);
        assertThat(queryList2.size(), is(3));
    }

    private String response() throws IOException {
        return Files.readString(Paths.get("src/test/resources/opa-compiler-response.json"));
    }

}
