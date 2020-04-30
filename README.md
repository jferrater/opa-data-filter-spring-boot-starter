# Data Filtering with Open Policy Agent and Spring Data MongoDb and JPA
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-spring-boot-starter)

### opa-datafilter-jpa-spring-boot-starter 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-datafilter-jpa-spring-boot-starter/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-datafilter-jpa-spring-boot-starter/0.4.4/jar)
[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-datafilter-jpa-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-datafilter-jpa-spring-boot-starter)

### opa-datafilter-mongo-spring-boot-starter
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jferrater/opa-datafilter-mongo-spring-boot-starter/badge.svg)](https://search.maven.org/artifact/com.github.jferrater/opa-datafilter-mongo-spring-boot-starter/0.4.4/jar)
[![SonarCloud Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=jferrater_opa-datafilter-mongo-spring-boot-starter&metric=alert_status)](https://sonarcloud.io/dashboard?id=jferrater_opa-datafilter-mongo-spring-boot-starter)

### Pre-requisites
The blog posts below explain enough of the What and Why!
- Read about Open Policy Agent Partial (OPA) Evaluation blog post [here](https://blog.openpolicyagent.org/partial-evaluation-162750eaf422)
- Read the sample use case, [Write Policy in OPA. Enforce Policy in SQL](https://blog.openpolicyagent.org/write-policy-in-opa-enforce-policy-in-sql-d9d24db93bf4)
- The [OPA Compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api)
<br>

### The Libraries
opa-datafilter-jpa-spring-boot-starter and opa-datafilter-mongo-spring-boot-starter are Spring Boot libraries which can be used together with Spring Data JPA and Spring Data MongoDB, respectively, to enforce authorization on data by filtering using OPA Partial Evaluation feature.
When a user wants to access a protected collection of resources, the libraries create a partial request object which contains about the user and the operation a user wants to perform. The partial request object is
sent to the OPA [compile API](https://www.openpolicyagent.org/docs/latest/rest-api/#compile-api).
OPA evaluates the partial request and returns a new and simplified policy that can be evaluated more efficiently than the original policy. These libraries convert
the new policy, the OPA compile API response, into SQL or MongoDB queries. A filtered collection of data is returned to the user which a user is allowed to see.

![Spring Boot App with OPA Data Filter](https://github.com/jferrater/opa-data-filter-spring-boot-starter/blob/master/diagram.png)

## Installation and Usage
See each individual folder for installation and usage instructions.
- [opa-datafilter-jpa-spring-boot-starter](opa-datafilter-jpa-spring-boot-starter/README.md)
- [opa-datafilter-mongo-spring-boot-starter](opa-datafilter-mongo-spring-boot-starter/README.md)


## Configurations
| Properties                                            | Type                | Default Value                    | Description                                                                                                                                                                                                                                                  | Required |
|-------------------------------------------------------|---------------------|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| opa.authorization.url                                 | String              | http://localhost:8181/v1/compile | The OPA compile API endpoint.                                                                                                                                                                                                                                | Yes      |
| opa.authorization.data-filter-enabled                 | Boolean             | true                             | Enable OPA data filter authorization                                                                                                                                                                                                                         | No       |
| opa.partial-request.log-partial-request               | Boolean             | false                            | Log the partial request json which was sent to OPA on std out for debugging                                                                                                                                                                                  | No       |
| opa.partial-request.query                             | String              |                                  | The query to partially evaluate and compile                                                                                                                                                                                                                  | Yes      |
| opa.partial-request.unknowns                          | Set<String>         |                                  | The terms to treat as unknown during partial evaluation                                                                                                                                                                                                      | Yes       |
| opa.partial-request.user-attribute-to-http-header-map | Map<String, String> |                                  | The mapping of user attribute to Http Header. These mappings will be added as subject attributes<br>in the input of the partial request. The key will be set as the attribute name and the value <br>of the Http header will be set as the value of the key. | No       |


## Example Spring Boot Applications
See example Spring Boot microservice applications that use these libraries:
 - [opa-datafilter-jpa-demo](https://github.com/jferrater/opa-data-filter-jpa-demo)
 - [opa-datafilter-mongo-demo](https://github.com/jferrater/opa-data-filter-mongo-demo)

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
			"jwt": "<jwt token>"
		}
	},
	"unknowns": ["data.pets"]
}
````
where: <br>
- `query` - is the value of `opa.partial-request.query` from the configuration property.
- `input` - by default http servlet path and method are added as `path` and `method` respectively. The `subject` (the current user) by default has `user` property
which is derived from Authorization Basic header if it exists and `jwt` property from the Authorization Bearer header if it exists.
- `unknowns` - the value of `opa.partial-request.unknowns` from the configuration property.

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

### Contributors
- [Joffry Ferrater](https://github.com/jferrater)
- [Reihmon Estremos](https://github.com/mongkoy)
- [Jolly Jae Ompod](https://github.com/ompodjol)
- [Alvin Difuntorum](https://github.com/alvinpd)

