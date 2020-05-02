## opa-datafilter-core
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-datafilter-core/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-datafilter-core/0.4.5/jar)
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter) [![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-datafilter-core&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-datafilter-core)

A Spring Boot library for translating Open Policy Agent (OPA) compile API response into JPA and Mongo queries.


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
