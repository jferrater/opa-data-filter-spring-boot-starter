package opa.datafilter.core.ast.db.query;

import com.fasterxml.jackson.databind.ObjectMapper;
import opa.datafilter.core.ast.db.query.model.request.PartialRequest;
import opa.datafilter.core.ast.db.query.service.OpaClientService;
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
import java.sql.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfiguration.class})
public class DataFilterIT {

    private static final String DOCKER_COMPOSE_YML = "src/integrationTest/resources/docker-compose.yml";
    private static final String POLICY_ENDPOINT = "/v1/policies";
    private static final String MARIA_DB = "maria-database_1";
    private static final String POSTGRESQL_DB = "postgresql-database_1";
    private static final String OPA = "opa-server_1";
    private static final int MARIA_DB_PORT = 3306;
    private static final int POSTGRESQL_DB_PORT = 5432;
    private static final int OPA_PORT = 8181;
    private static final String MARIADB_JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String MARIADB_JDBC_URL = "jdbc:mariadb://localhost:3306/integrationTest";
    private static final String POSTGRESQL_JDBC_URL = "jdbc:postgresql://localhost:5432/integrationTest";
    private static final String POSTGRES_DRIVER = "org.postgresql.Driver";
    private static final String USER = "admin";
    private static final String PASSWORD = "MangaonTaNiny0!";

    @Autowired
    OpaClientService target;

    @ClassRule
    public static DockerComposeContainer environment = new DockerComposeContainer(new File(DOCKER_COMPOSE_YML))
            .withExposedService(MARIA_DB, MARIA_DB_PORT, Wait.forListeningPort())
            .withExposedService(POSTGRESQL_DB, POSTGRESQL_DB_PORT, Wait.forListeningPort())
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

    @DisplayName(
            "Given a MariaDB with initialized data from classpath:sql/initData.sql" +
            "When the partial request is translated into sql query" +
            "And the translated sql query is executed to the MariaDB" +
            "The MariaDB should response result sets"
    )
    @Test
    public void shouldBeAbleToQueryTheMariaDatabaseUsingTheSqlStatementFromOpa() throws Exception {
        PartialRequest partialRequest = getPartialRequest();
        String sqlStatements = target.getExecutableSqlStatements(partialRequest);
        sendSqlQueryToTheDatabase(sqlStatements, MARIADB_JDBC_DRIVER, MARIADB_JDBC_URL);
    }

    @DisplayName(
            "Given a Postgresql DB with initialized data from classpath:sql/initData.sql" +
            "When the partial request is translated into sql query" +
            "And the translated sql query is executed to the Postgresql DB" +
            "The Postgresql DB should response result sets"
    )
    @Test
    public void shouldBeAbleToQueryThePostgresqlDatabaseUsingTheSqlStatementFromOpa() throws Exception {
        PartialRequest partialRequest = getPartialRequest();
        String sqlStatements = target.getExecutableSqlStatements(partialRequest);
        sendSqlQueryToTheDatabase(sqlStatements, POSTGRES_DRIVER, POSTGRESQL_JDBC_URL);
    }

    private void sendSqlQueryToTheDatabase(String sqlStatements, String databaseDrivder, String jdbcUrl) throws ClassNotFoundException, SQLException {
        Class.forName(databaseDrivder);
        try (Connection connection = DriverManager.getConnection(jdbcUrl, USER, PASSWORD)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet result = statement.executeQuery(sqlStatements)) {
                    while (result.next()) {
                        String name = result.getString("name");
                        assertThat(name, is("fluffy"));
                        String veterinarian = result.getString("veterinarian");
                        assertThat(veterinarian, is("alice"));
                        String owner = result.getString("owner");
                        assertThat(owner, is("alice"));
                        String clinic = result.getString("clinic");
                        assertThat(clinic, is("SOMA"));
                    }
                }
            }
        }
    }

    private PartialRequest getPartialRequest() throws IOException {
        String opaCompilerResponseInString = Files.readString(Paths
                .get("src/integrationTest/resources/pets-clinic-partial-request.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(opaCompilerResponseInString, PartialRequest.class);
    }
}
