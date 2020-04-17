package com.github.jferrater.opa.ast.db.query.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "opa.authorization.datasource.jdbc")
@Data
@NoArgsConstructor
public class Datasource {

    // Default values set for H2 Database
    /**
     * The datasource JDBC url
     */
    private String url = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1";
    /**
     * The datasource username
     */
    private String username = "sa";
    /**
     * The datasource password
     */
    private String password = "";
    /**
     * The datasource JDBC driver class name
     */
    private String driverClassName = "org.h2.Driver";
}
