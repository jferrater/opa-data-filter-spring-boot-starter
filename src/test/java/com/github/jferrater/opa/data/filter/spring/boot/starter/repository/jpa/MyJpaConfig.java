package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.entity",
"com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa"},
repositoryBaseClass = OpaRepositoryImpl.class, repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class)
public class MyJpaConfig {

}
