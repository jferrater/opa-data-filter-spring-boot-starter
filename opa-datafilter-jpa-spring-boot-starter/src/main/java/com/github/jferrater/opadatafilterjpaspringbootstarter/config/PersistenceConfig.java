package com.github.jferrater.opadatafilterjpaspringbootstarter.config;

import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.OpaDataFilterRepositoryImpl;
import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.OpaRepositoryFactoryBean;
import opa.datafilter.core.ast.db.query.config.OpaDataCoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author joffryferrater
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {"com.github.jferrater.opadatafilterjpaspringbootstarter.repository"},
        repositoryBaseClass = OpaDataFilterRepositoryImpl.class, repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class)
@Import(OpaDataCoreConfig.class)
public class PersistenceConfig {

}