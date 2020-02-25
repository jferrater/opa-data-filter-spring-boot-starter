package com.joffryferrater.opadatafilterspringbootstarter.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joffryferrater.opadatafilterspringbootstarter.model.response.OpaCompilerResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestBase {


    public OpaCompilerResponse opaCompilerResponse() throws IOException {
        String opaCompilerResponseInString = Files.readString(Paths.get("src/test/resources/opa-compiler-response.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(opaCompilerResponseInString, OpaCompilerResponse.class);
    }
}
