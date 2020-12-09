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
        String opaCompilerResponseInString = getFileFromResourcesAsString("src/test/resources/opa-compiler-response.json");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(opaCompilerResponseInString, OpaCompilerResponse.class);
    }

    public static OpaCompilerResponse opaV23CompilerResponse() throws IOException {
        String opaCompilerResponseInString = getFileFromResourcesAsString("src/test/resources/opa-compiler-response-v025.json");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(opaCompilerResponseInString, OpaCompilerResponse.class);
    }

    private static String getFileFromResourcesAsString(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }
}
