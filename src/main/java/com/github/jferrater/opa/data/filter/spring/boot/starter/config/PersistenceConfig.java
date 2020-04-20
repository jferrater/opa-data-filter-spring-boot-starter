package com.github.jferrater.opa.data.filter.spring.boot.starter.config;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaRepositoryFactoryBean;
import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaDataFilterRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(repositoryBaseClass = OpaDataFilterRepositoryImpl.class, repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class)
public class PersistenceConfig {

    private Environment environment;
    private DataSource dataSource;

    public PersistenceConfig(Environment environment, DataSource dataSource) {
        this.environment = environment;
        this.dataSource = dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        String packageNameProperty = environment.getRequiredProperty("opa.datasource.entities-package-name");
        entityManagerFactoryBean.setPackagesToScan("com.github.jferrater.opa.data.filter.spring.boot.starter.repository.hibernate",
                "com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa", packageNameProperty);
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

}