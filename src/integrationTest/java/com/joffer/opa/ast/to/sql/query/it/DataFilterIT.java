package com.joffer.opa.ast.to.sql.query.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joffer.opa.ast.to.sql.query.model.request.PartialRequest;
import com.joffer.opa.ast.to.sql.query.service.OpaClientService;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfiguration.class})
public class DataFilterIT {

    private static final String DOCKER_COMPOSE_YML = "src/integrationTest/resources/docker-compose.yml";
    private static final String POLICY_ENDPOINT = "/v1/policies";
    private static final String MARIA_DB = "maria-database_1";
    private static final String OPA = "opa-server_1";
    private static final int MARIA_DB_PORT = 3306;
    private static final int OPA_PORT = 8181;

    @Autowired
    OpaClientService target;

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(new File(DOCKER_COMPOSE_YML))
                    .withExposedService(MARIA_DB, MARIA_DB_PORT, Wait.forListeningPort())
                    .withExposedService(OPA, OPA_PORT, Wait.forHttp(POLICY_ENDPOINT)
                            .forStatusCode(200))
                            .withLocalCompose(true);

    @BeforeAll
    public static void start() {
        environment.start();
    }

    @AfterAll
    public static void stop() {
        environment.stop();
    }

    @DisplayName(
            "Given a partial request classpath:pets-clinic-partial-request.json" +
            "When the request is sent to the Open Policy Agent Server compile API" +
            "I should receive the sql statements in string"
    )
    @Test
    public void shouldGetTheSqlQueryStatements() throws IOException {
        PartialRequest partialRequest = getPartialRequest();
        String result = target.getExecutableSqlStatements(partialRequest);
        assertThat(result, is("SELECT * FROM pets WHERE (pets.owner = 'alice' AND pets.name = 'fluffy') OR (pets.veterinarian = 'alice' AND pets.clinic = 'SOMA' AND pets.name = 'fluffy');"));
    }

    private PartialRequest getPartialRequest() throws IOException {
        String opaCompilerResponseInString = Files.readString(Paths
                .get("src/integrationTest/resources/pets-clinic-partial-request.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(opaCompilerResponseInString, PartialRequest.class);
    }
}
