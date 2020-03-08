# opa-ast-to-sql-query-java 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-ast-to-sql-query-java/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-ast-to-sql-query-java/0.1.1/jar)
[![Build Status](https://travis-ci.com/jferrater/opa-ast-to-sql-query-java.svg?branch=master)](https://travis-ci.com/jferrater/opa-ast-to-sql-query-java)[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-ast-to-sql-query-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-ast-to-sql-query-java)

opa-ast-to-sql-query-java is a Spring Boot library that can be used in Spring Boot application to translate Open Policy Agent (OPA) Abstract Syntax Tree (AST) to a simple SQL statements in string format.

OPA supports partial evaluation through its [compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api). The compile API response is a set of queries which is an AST.
See this blog post for more info, [Write Policy in OPA. Enforce Policy in SQL](https://blog.openpolicyagent.org/write-policy-in-opa-enforce-policy-in-sql-d9d24db93bf4). The integration test of this project is based on the blog mentioned.

### Usage
1. Add dependency from Maven Central. For gradle project:
```groovy
implementation group:'com.github.jferrater', name: 'opa-ast-to-sql-query-java', version: '0.1.1'
````  
or for maven project:
````xml
<dependency>
    <groupId>com.github.jferrater</groupId>
    <artifactId>opa-ast-to-sql-query-java</artifactId>
    <version>0.1.1</version>
</dependency>
````
2. Add the Open Policy Agent compile API endpoint to the application.yml or application.properties of the Spring Boot project.
````yaml
opa:
  authorization:
    url: http://localhost:8181/v1/compile
````
3. Autowire the ``OpaClientService`` class to invoke the `getExecutableSqlStatements``. See example code snippet:

```java
package com.example.opadatafilterdemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jferrater.opa.ast.to.sql.query.model.request.PartialRequest;
import com.github.jferrater.opa.ast.to.sql.query.service.OpaClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    @Autowired
    private OpaClientService opaClientService;

    public String getSqlStatementsFromOpa() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        PartialRequest partialRequest = objectMapper.readValue(partialRequest(), PartialRequest.class);
        return opaClientService.getExecutableSqlStatements(partialRequest);
    }

    private String partialRequest() {
        return "{\n" +
                "    \"query\": \"data.petclinic.authz.allow = true\",\n" +
                "    \"input\": {\n" +
                "      \"method\": \"GET\",\n" +
                "      \"path\": [\"pets\", \"fluffy\"],\n" +
                "      \"subject\": {\n" +
                "        \"user\": \"alice\",\n" +
                "        \"location\": \"SOMA\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"unknowns\": [\"data.pets\"]\n" +
                "  }";
    }
}
```

### Running the Integration Test
The integration test has a sample use case from the blog, [Write Policy in OPA. Enforce Policy in SQL](https://blog.openpolicyagent.org/write-policy-in-opa-enforce-policy-in-sql-d9d24db93bf4)
1. Install docker-compose
2. Run gradle command:
```console
./gradlew integrationTest
```

### On going:
- support for SQL dialect
- support for OPA AST data types
