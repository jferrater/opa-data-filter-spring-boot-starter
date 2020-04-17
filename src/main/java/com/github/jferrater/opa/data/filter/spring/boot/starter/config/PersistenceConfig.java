package com.github.jferrater.opa.data.filter.spring.boot.starter.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class PersistenceConfig {

    private Environment environment;

    public PersistenceConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @ConditionalOnMissingBean
    public LocalSessionFactoryBean sessionFactory() {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        String packageNameProperty = environment.getRequiredProperty("opa.authorization.datasource.hibernate.entities.package-name");
        sessionFactory.setPackagesToScan("com.github.jferrater.opa.data.filter.spring.boot.starter.repository.hibernate", packageNameProperty);
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(environment.getProperty("opa.authorization.datasource.jdbc.driverClassName"));
        dataSource.setJdbcUrl(environment.getProperty("opa.authorization.datasource.jdbc.url"));
        dataSource.setUsername(environment.getProperty("opa.authorization.datasource.jdbc.username"));
        dataSource.setPassword(environment.getProperty("opa.authorization.datasource.jdbc.password"));
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public PlatformTransactionManager hibernateTransactionManager() {
        final HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        String dialectProperty = environment.getProperty("opa.authorization.datasource.hibernate.dialect");
        hibernateProperties.setProperty("hibernate.dialect", Objects.requireNonNull(dialectProperty, "Required Hibernate property 'hibernate.dialect' is missing."));
        String showSqlProperty = environment.getProperty("opa.authorization.datasource.hibernate.show_sql");
        hibernateProperties.setProperty("hibernate.show_sql", showSqlProperty == null ? "false" : "true");
        String cacheFactoryClassProperty = environment.getProperty("opa.authorization.datasource.hibernate.cache.region.factory_class");
        hibernateProperties.setProperty("hibernate.cache.region.factory_class", cacheFactoryClassProperty == null ? "org.hibernate.cache.ehcache.EhCacheRegionFactory" : cacheFactoryClassProperty);
        String levelCacheProperty = environment.getProperty("opa.authorization.datasource.hibernate.cache.use_second_level_cache");
        hibernateProperties.setProperty("hibernate.cache.use_second_level_cache", levelCacheProperty == null ? "true" : levelCacheProperty);
        String useQueryProperty = environment.getProperty("opa.authorization.datasource.hibernate.cache.use_query_cache");
        hibernateProperties.setProperty("hibernate.cache.use_query_cache", useQueryProperty == null ? "true" : useQueryProperty);
        hibernateProperties.setProperty("hibernate.cache.ehcache.missing_cache_strategy", "create");
        return hibernateProperties;
    }
}