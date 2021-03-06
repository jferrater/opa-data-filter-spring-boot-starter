package com.github.jferrater.opadatafiltermongospringbootstarter.config;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaDataFilterMongoRepositoryImpl;
import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaMongoRepositoryFactoryBean;
import opa.datafilter.core.ast.db.query.config.OpaDataCoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author joffryferrater
 */
@Configuration
@EnableMongoRepositories(
        basePackages = {"com.github.jferrater.opadatafiltermongospringbootstarter.repository"},
        repositoryBaseClass = OpaDataFilterMongoRepositoryImpl.class,
        repositoryFactoryBeanClass = OpaMongoRepositoryFactoryBean.class
)
@Import(OpaDataCoreConfig.class)
public class MongoRepositoryConfig {
}
