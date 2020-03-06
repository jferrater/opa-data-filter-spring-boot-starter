# opa-ast-to-sql-query-java

Open Policy Agent (OPA) supports partial evaluation through its [compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api).
The compile API response is a set of queries which is an Abstract Syntax Tree (AST).
See this blog post, [Write Policy in OPA. Enforce Policy in SQL](https://blog.openpolicyagent.org/write-policy-in-opa-enforce-policy-in-sql-d9d24db93bf4)
for more info. The integration test of this project is based on the blog mentioned.

opa-ast-to-sql-query-java is a Spring Boot library that can be used in Spring Boot application to translate OPA AST to a simple SQL statements in string format.

### Building the project
```console    
./gradlew clean build
```
### Publishing to Maven local
```console
./gradlew publishToMavenLocal
```
### Running the Integration Test
1. Install docker-compose
2. Run gradle command:
```console
./gradlew integrationTest
```
### Usage
1. Add the dependency to the Spring Boot project. Make sure to add mavenLocal() as a repository
```groovy
implementation group:'com.joffer', name: 'opa-ast-to-sql-query-java', version: '0.1'
````  
2. Add the Open Policy Agent compile API endpoint to the application.yml or application.properties
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
import com.joffer.opa.ast.to.sql.query.model.request.PartialRequest;
import com.joffer.opa.ast.to.sql.query.service.OpaClientService;
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

### To be added
- support for OPA AST data types