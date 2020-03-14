# opa-data-filter-spring-boot-starter 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-ast-to-sql-query-java/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-ast-to-sql-query-java/0.1.1/jar)
[![Build Status](https://travis-ci.com/jferrater/opa-ast-to-sql-query-java.svg?branch=master)](https://travis-ci.com/jferrater/opa-ast-to-sql-query-java)[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-ast-to-sql-query-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-ast-to-sql-query-java)

opa-data-filter-spring-boot-starter is a Spring Boot starter project that can be used in Spring Boot application to integrate Open Policy Agent (OPA) partial evaluation feature. The library sends partial request to the OPA compile API.
OPA evaluates the partial request against a policy and return a response which is an Abstract Syntax Tree (AST). This starter project translates the AST into an executable SQL query. The SQL query is used in the Hibernate DAO layer 
which will be send to the database.

OPA supports partial evaluation through its [compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api). The compile API response is a set of queries which is an AST.
See this blog post for more info, [Write Policy in OPA. Enforce Policy in SQL](https://blog.openpolicyagent.org/write-policy-in-opa-enforce-policy-in-sql-d9d24db93bf4). The integration test of this project is based on the blog mentioned.

### Installation
Add library to the Spring Boot project. For gradle project:
```groovy
implementation group:'com.github.jferrater', name: 'opa-data-filter-spring-boot-starter', version: '0.2.1'
````  
or maven:
````xml
<dependency>
    <groupId>com.github.jferrater</groupId>
    <artifactId>opa-data-filter-spring-boot-starter</artifactId>
    <version>0.2.1</version>
</dependency>
````

### Usage
1. Add the following minimum configuration to the application.yml or application.properties of the Spring Boot project. Replace the value as necessary
````yaml
server:
  port: 8081
opa:
  authorization:
    data-filter-enabled: true
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

A sample project will be created soon.


### Building the project
``./gradlew clean build``

### Running the integration test
1. Install docker-compose (Note: the docker-compose version used for this build is 1.25.4. Some version may not be compatible with TestContainer framework)
2. ``./gradlew integrationTest``

### Tested on databases (refer to the integration tests):
- MariaDB
- Postgresql

### Ongoing development:
- Investigate and add mappings of OPA AST data types

### Contributors
- [Joffry Ferrater](https://github.com/jferrater)
- [Reihmon Estremos](https://github.com/mongkoy)
- [Jolly Jae Ompod](https://github.com/ompodjol)
- [Alvin Difuntorum](https://github.com/alvinpd)