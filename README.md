# opa-data-filter-spring-boot-starter 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-data-filter-spring-boot-starter/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-data-filter-spring-boot-starter/0.4.2/jar)
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter) [![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-data-filter-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-data-filter-spring-boot-starter)

### Pre-requisites
The blog posts below explain enough of the What and Why!
- Read about Open Policy Agent Partial (OPA) Evaluation blog post [here](https://blog.openpolicyagent.org/partial-evaluation-162750eaf422)
- Read the sample use case, [Write Policy in OPA. Enforce Policy in SQL](https://blog.openpolicyagent.org/write-policy-in-opa-enforce-policy-in-sql-d9d24db93bf4)
- The [OPA Compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api)
<br>

### The Library
opa-data-filter-spring-boot-starter is a Spring Boot library which can be used together with Spring Data JPA and Spring Data MongoDB to secure data by filtering using OPA Partial Evaluation feature.
When a user wants to access a protected collection of resources, the library creates a partial request object which contains about the user and the operation a user wants to perform. The partial request object is
sent to the OPA [compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api).
OPA evaluates the partial request and returns a new and simplified policy that can be evaluated more efficiently than the original policy. This library converts
the new policy, the OPA compile API response, into SQL or MongoDB queries. A filtered collection of data is returned to the user which a user is allowed to see.

![Spring Boot App with OPA Data Filter](https://github.com/jferrater/opa-data-filter-spring-boot-starter/blob/master/diagram.png)

## Installation
For Spring Data JPA, <br>
gradle project:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
implementation group:'com.github.jferrater', name: 'opa-data-filter-spring-boot-starter', version: '0.4.2'
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
    <version>0.4.2</version>
</dependency>
````

## Usage
1. Add the following minimum configuration to the application.yml or application.properties of the Spring Boot project. Replace the values as necessary. See `Configurations` section for more details.
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
2. Create a sub interface of `OpaDataFilterRepository`. This repository is a custom Spring Data JPA repository which overrides the `findAll()`
method to enforce authorization. The method sends a partial request to the OPA server. The response from OPA is converted into TypedQuery
which will be used by Spring Data JPA to filter results. The filtered results are the data the user is allowed to see.

```java
package com.example.opadatafilterdemo.repository;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.OpaDataFilterRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends OpaRepository<PetProfileEntity, Long> {

}
```
#### The managed entity class
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
3. Finally, configure JPA. Note that `repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class` is required in the `@EnableJpaRepositories`.
````java
@Configuration
@EnableJpaRepositories(
        value = "com.example.opadatafilterdemo.repository",
        repositoryFactoryBeanClass = OpaRepositoryFactoryBean.class
)
public class JpaEnvConfig {

}
````
## Configurations
| Properties                                            | Type                | Default Value                    | Description                                                                                                                                                                                                                                                  | Required |
|-------------------------------------------------------|---------------------|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| opa.authorization.url                                 | String              | http://localhost:8181/v1/compile | The OPA compile API endpoint.                                                                                                                                                                                                                                | Yes      |
| opa.authorization.data-filter-enabled                 | Boolean             | true                             | Enable OPA data filter authorization                                                                                                                                                                                                                         | No       |
| opa.partial-request.log-partial-request               | Boolean             | false                            | Log the partial request json which was sent to OPA on std out for debugging                                                                                                                                                                                  | No       |
| opa.partial-request.query                             | String              |                                  | The query to partially evaluate and compile                                                                                                                                                                                                                  | Yes      |
| opa.partial-request.unknowns                          | Set<String>         |                                  | The terms to treat as unknown during partial evaluation                                                                                                                                                                                                      | No       |
| opa.partial-request.user-attribute-to-http-header-map | Map<String, String> |                                  | The mapping of user attribute to Http Header. These mappings will be added as subject attributes<br>in the input of the partial request. The key will be set as the attribute name and the value <br>of the Http header will be set as the value of the key. | No       |
## Example Spring Boot Application
See example Spring Boot project that uses this library --> [opa-data-filter-demo](https://github.com/jferrater/opa-data-filter-demo)

 Integration with MongoDB instructions and sample project will be added soon!

## The Partial Request
### Default Partial Request
 When the `findAll` method is invoked from `OpaDataFilterRepository` interface, a partial request object is sent
 to the [OPA compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api) endpoint. The default partial request is the following:
 ````json
{
	"query": "data.petclinic.authz.allow = true",
	"input": {
		"path": ["pets"],
		"method": "GET",
		"subject": {
			"user": "alice",
			"jwt": ""
		}
	},
	"unknowns": ["data.pets"]
}
````
where: <br>
- `query` - is the value of `opa.partial-request.query` from the configuration property
- `input` - by default http servlet path and method are added as `path` and `method` respectively. The `subject` (the current user) by default has `user` property
which is derived from Authorization Basic header if it exists and `jwt` property from the Authorization Bearer header if it exists.
- `unknowns` - the value of `opa.partial-request.unknowns` from the configuration property if configured. This is optional.
### Adding Attributes to the Default Partial Request
It is possible to add attributes to the `subject` property of the default partial request. This can be done by mapping the attribute to the Http header using
a configuration `opa.partial-request.user-attribute-to-http-header-map`. For example:
````yaml
opa:
  partial-request:
    log-partial-request: true
    user-attribute-to-http-header-map:
      organization: X-ORG-HEADER
````
The `organization` is key of the attribute and the value of the `X-ORG-HEADER` http header is the value of the key. The partial request will
look like the following:
````json
{
	"query": "data.petclinic.authz.allow = true",
	"input": {
		"path": ["pets"],
		"method": "GET",
		"subject": {
			"user": "alice",
			"attributes": {
				"organization": "SOMA"
			}
		}
	},
	"unknowns": ["data.pets"]
}
````
## Development
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
