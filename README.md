# opa-data-filter-spring-boot-starter 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-data-filter-spring-boot-starter/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-data-filter-spring-boot-starter/0.2.2/jar)
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter) [![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-data-filter-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-data-filter-spring-boot-starter)

### Pre-requisites
The blog posts below explain enough of the What and Why!
- Read about Open Policy Agent Partial (OPA) Evaluation blog post [here](https://blog.openpolicyagent.org/partial-evaluation-162750eaf422)
- Read the sample use case, [Write Policy in OPA. Enforce Policy in SQL](https://blog.openpolicyagent.org/write-policy-in-opa-enforce-policy-in-sql-d9d24db93bf4)
- The [OPA Compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api)
<br>

### The Library
opa-data-filter-spring-boot-starter is a Spring Boot library which can be used together with Spring Data Hibernate/JPA and Spring Data MongoDB to secure data using OPA Partial Evaluation feature.
When a user wants to access a protected collection of resources, the library creates a partial request object which contains about the user and the operation a user wants to perform. The partial request object is
sent to the OPA[compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api).
OPA evaluates the partial request and returns a new and simplified policy that can be evaluated more efficiently than the original policy. This library converts
the new policy into SQL or MongoDB queries. A collection of data is returned to the user which a user is allowed to see.

![Spring Boot App with OPA Data Filter](https://github.com/jferrater/opa-data-filter-spring-boot-starter/blob/master/diagram.png)

### Installation
For Spring Data JPA/Hibernate, <br>
gradle project:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation group:'com.github.jferrater', name: 'opa-data-filter-spring-boot-starter', version: '0.2.2'
```
or maven:
````xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.github.jferrater</groupId>
    <artifactId>opa-data-filter-spring-boot-starter</artifactId>
    <version>0.2.2</version>
</dependency>
````

### Usage
1. Add the following minimum configuration to the application.yml or application.properties of the Spring Boot project. Replace the value as necessary
````yaml
opa:
  authorization:
    url: "http://localhost:8181/v1/compile"
    datasource:
      jdbc:
        driverClassName: "org.h2.Driver"
        username: "sa"
        password: ""
        url: "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:init.sql'"
      hibernate:
        dialect: "org.hibernate.dialect.H2Dialect"
        entities:
          package-name: "com.example.opadatafilterdemo.entity"
````
2. Create a repository class:

```java
package com.example.opadatafilterdemo;

import com.example.opadatafilterdemo.entity.PetEntity;
import com.github.jferrater.opa.data.filter.spring.boot.starter.OpaGenericDataFilterDao;
import org.springframework.stereotype.Repository;

@Repository
public class PetRepository extends OpaGenericDataFilterDao<PetEntity> {

    public PetRepository() {
        setClazz(PetEntity.class);
    }
}

```
and the entity class
````java
package com.example.opadatafilterdemo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pets")
public class PetEntity {

    @Id
    private Long id;
    private String name;
    private String owner;
    private String veterinarian;
    private String clinic;

    // getters and setters 
}
````

### Example Spring Boot Application
See example Spring Boot project that uses this library --> [opa-data-filter-demo](https://github.com/jferrater/opa-data-filter-demo)

 Integration with MongoDB instructions and sample project will be added soon!

### Building the project
``./gradlew clean build``

### Running the integration test
1. Install docker-compose (Note: the docker-compose version used for this build is 1.25.4. Some version may not be compatible with TestContainer framework)
2. ``./gradlew integrationTest``

### Tested on databases (refer to the integration tests):
- MariaDB
- Postgresql
- MongoDB

### Contributors
- [Joffry Ferrater](https://github.com/jferrater)
- [Reihmon Estremos](https://github.com/mongkoy)
- [Jolly Jae Ompod](https://github.com/ompodjol)
- [Alvin Difuntorum](https://github.com/alvinpd)
