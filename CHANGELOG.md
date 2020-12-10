# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.5.1] - 2020-12-10
- Fixed the incompatibility issue from OPA version 0.23 and above

## [0.4.5] - 2020-05-02
- JPA: findAll(Sort sort) implementation of OpaDataFilterRepository
- MongoDB: findAll(Sort sort) implementation of OpaDataFilterMongoRepository
- Updated opa-datafilter-core dependency to 0.4.6 which added gte and lte opa boolean operator
- Pretty json logs of partial request
- JPA: findAll(Pageable pageable) implementation of OpaDatafilterRepository
- MongoDB: findAll(Pageable pageable) implementation of OpaDataFilterMongoRepository
- Fixes integer, long and double assignments in sql statements:

## [0.4.4] - 2020-04-21
### Added
- individual releases of opa-datafilter-core, opa-datafilter-jpa-spring-boot-starter and opa-datafilter-mongo-spring-boot-starter
- JPA findAll() implementation of OpaDataFilterRepository
- MongoDb findAll() implementation of OpaDataFilterMongoRepository
