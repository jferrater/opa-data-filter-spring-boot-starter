package com.github.jferrater.opadatafiltermongospringbootstarter.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoConfig {
    @Value("${spring.data.mongodb.uri}")
    private String connectionUrl;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoClient mongo() {
        return MongoClients.create(connectionUrl);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), databaseName);
    }

}
