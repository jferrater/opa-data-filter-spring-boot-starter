## opa-datafilter-jpa-spring-boot-starter
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-datafilter-jpa-spring-boot-starter/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-datafilter-jpa-spring-boot-starter/0.4.5/jar)
[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-datafilter-jpa-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-datafilter-jpa-spring-boot-starter)

## Installation
gradle project:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation group:'com.github.jferrater', name: 'opa-datafilter-jpa-spring-boot-starter', version: '0.4.5'
```
or maven:
````xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.github.jferrater</groupId>
    <artifactId>opa-datafilter-jpa-spring-boot-starter</artifactId>
    <version>0.4.5</version>
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

# Spring Data JPA specific configurations
spring:
  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3306/integrationTest
    username: admin
    password: MangaonTaNiny0!
````
- Create a sub interface of `OpaDataFilterRepository`. This repository is a sub-interface of Spring Data JPA repository which overrides the `findAll()`
method to enforce authorization. The `findAll()` method sends a partial request to the OPA server. The response from OPA which is a simplified version
of the policy is translated into `TypedQuery` object which will be used by Spring Data JPA to filter results. The filtered results are the data the user is allowed to see.

```java
package com.example.opadatafilterdemo.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.OpaDataFilterRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends OpaDataFilterRepository<PetProfileEntity, Long> {

}
```
where the managed entity:
````java
package com.example.opadatafilterdemo.repository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pets")
public class PetProfileEntity {

    @Id
    private Long id;
    private String name;
    private String owner;
    private String veterinarian;
    private String clinic;

    // getters and setters 
}
````
- Finally, enable JPA repository. Note that `repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class` is required in the `@EnableJpaRepositories`.
````java
import com.github.jferrater.opadatafilterjpaspringbootstarter.repository.OpaRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        value = "com.example.opadatafilterdemo.repository",
        repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class
)
public class JpaEnvConfig {

}
````

## Example Spring Boot Application
See example Spring Boot microservice application that uses the library:
 - [opa-datafilter-jpa-demo](https://github.com/jferrater/opa-data-filter-jpa-demo)
