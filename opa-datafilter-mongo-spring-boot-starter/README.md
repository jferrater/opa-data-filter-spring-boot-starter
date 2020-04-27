## opa-datafilter-mongo-spring-boot-starter
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-datafilter-mongo-spring-boot-starter/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-datafilter-mongo-spring-boot-starter/0.4.4/jar)
[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-datafilter-mongo-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-datafilter-mongo-spring-boot-starter)

## Installation
gradle project:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-mongodb'
implementation group:'com.github.jferrater', name: 'opa-datafilter-mongo-spring-boot-starter', version: '0.4.4'
```
or maven:
````xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
<dependency>
    <groupId>com.github.jferrater</groupId>
    <artifactId>opa-datafilter-mongo-spring-boot-starter</artifactId>
    <version>0.4.4</version>
</dependency>
````

## Usage
- Add the following minimum configuration to the application.yml or application.properties of the Spring Boot project. Replace the values as necessary. See `Configurations` section for more details.
````yaml
opa:
  authorization:
    url: "http://localhost:8181/v1/compile"
  partial-request:
    query: "data.petclinic.authz.allow = true"
    unknowns:
      - "data.pets"

# Spring Data MongoDB specific configurations
spring:
  data:
    mongodb:
      database: user_service
      uri: "mongodb://mongo:mongo@mongo-database:27017/user_service"
````

- Create a sub interface of `OpaDataFilterMongoRepository`. This repository is a sub-interface of Spring Data MongoDB repository which overrides the `findAll()`
method to enforce authorization. The `findAll()` method sends a partial request to the OPA server. The response from OPA which is a simplified version
of the policy is translated into `Query` object which will be used by Spring Data MongoDB to filter results. The filtered results are the data the user is allowed to see.

```java
package com.github.jferrater.userservice.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaDataFilterMongoRepository;
import com.github.jferrater.userservice.repository.document.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends OpaDataFilterMongoRepository<User, String> {

    List<User> findByOrganizationAndUsername(String organization, String username);
}
```
where the document object:
````java
package com.github.jferrater.userservice.repository.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    @Indexed
    private String organization;
    @Indexed(unique = true)
    private String username;
    private String password;
    private String[] roles;
    private String[] permissions;
    
    //getters & setters

}
````
- Finally, enable MongoDB repository. Note that `repositoryFactoryBeanClass = OpaMongoRepositoryFactoryBean.class` is required in the `@EnableMongoRepositories`.
````java
package com.github.jferrater.userservice.config;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.OpaMongoRepositoryFactoryBean;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.github.jferrater.userservice.repository"
        , repositoryFactoryBeanClass = OpaMongoRepositoryFactoryBean.class
)
public class MongoDbConfig {

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
````

## Example Spring Boot Applications
See example Spring Boot microservice applicatio that use the library:
 - [opa-datafilter-mongo-demo](https://github.com/jferrater/opa-data-filter-mongo-demo)