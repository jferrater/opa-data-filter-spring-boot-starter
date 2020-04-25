package com.github.jferrater.opadatafiltermongospringbootstarter.config;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaDataFilterMongoRepositoryImpl;
import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaMongoRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = {"com.github.jferrater.opadatafiltermongospringbootstarter.repository"},
        repositoryBaseClass = OpaDataFilterMongoRepositoryImpl.class,
        repositoryFactoryBeanClass = OpaMongoRepositoryFactoryBean.class
)
public class MongoRepositoryConfig {
}
