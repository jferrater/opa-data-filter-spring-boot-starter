package opa.datafilter.core.ast.db.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import opa.datafilter.core.ast.db.query.model.response.OpaCompilerResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author joffryferrater
 */
public class TestBase {


    public static OpaCompilerResponse opaCompilerResponse() throws IOException {
        String opaCompilerResponseInString = Files.readString(Paths.get("src/test/resources/opa-compiler-response.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(opaCompilerResponseInString, OpaCompilerResponse.class);
    }
}
