package com.github.jferrater.opa.ast.db.query.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "opa.datasource")
@Data
@NoArgsConstructor
public class Datasource {
    /**
     * The package name of the entities to scan for the EntityManagerFactoryBean
     */
    @NotNull
    private String entitiesPackageName;
}
