## opa-datafilter-query-service
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter) [![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-datafilter-query-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-datafilter-query-service)
<br>
A service that provides API which translates Open Policy Agent (OPA) [Partial Request](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api) into a SQL or MongoDB query.

### Pre-requisite
- Open Policy Agent Server v0.22.0

### Quick Start
1. `git clone https://github.com/jferrater/opa-data-filter-spring-boot-starter.git`
2. `cd opa-datafilter-query-service`
3. Update `opa.authorization.url` in the src/main/resources/application.yml file. Default value is `http://localhost:8181/v1/compile`.
3. `./gradlew bootRun`
4. API documentation at http://localhost:8727/opa-query-service/v1/swagger-ui.html


