package com.joffryferrater.opadatafilterspringbootstarter.it;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataFilterIT {

    private static final String DOCKER_COMPOSE_YML = "src/integrationTest/resources/docker-compose.yml";
    private static final String POLICY_ENDPOINT = "/v1/policies";
    private static final String MARIA_DB = "maria-database_1";
    private static final String OPA = "opa-server_1";
    private static final int MARIA_DB_PORT = 3306;
    private static final int OPA_PORT = 8181;

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

    @Test
    public void shouldRun() {
        System.out.println("hello world!");
        assertThat(1, is(1));
    }

}
