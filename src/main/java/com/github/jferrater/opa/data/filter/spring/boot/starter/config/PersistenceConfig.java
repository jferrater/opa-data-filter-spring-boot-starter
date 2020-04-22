package com.github.jferrater.opa.data.filter.spring.boot.starter.config;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaDataFilterRepositoryImpl;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa",
        repositoryBaseClass = OpaDataFilterRepositoryImpl.class, repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class)
public class PersistenceConfig {

}