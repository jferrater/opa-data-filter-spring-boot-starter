package com.github.jferrater.opa.ast.db.query.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "opa.authorization.datasource.hibernate")
@Data
@NoArgsConstructor
public class Hibernate {

    /**
     * The Hibernate Dialect to use
     */
    private String dialect;
    /**
     * Enable showing of sql query
     */
    private boolean showSql = false;
    private Cache cache;
    private Entities entities;

    @Data
    public static class Entities {

        /**
         * The package name of the JPA entity classes
         */
        private String packageName;
    }
}
