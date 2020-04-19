package com.github.jferrater.opa.data.filter.spring.boot.starter.config;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepositoryFactoryBean;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepositoryImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(repositoryBaseClass = OpaRepositoryImpl.class, repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class)
public class JpaConfig {
}
