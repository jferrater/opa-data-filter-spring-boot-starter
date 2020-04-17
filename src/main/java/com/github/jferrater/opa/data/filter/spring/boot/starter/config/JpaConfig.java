package com.github.jferrater.opa.data.filter.spring.boot.starter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa")
public class JpaConfig {
}
