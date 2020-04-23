package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.mongodb;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.mongodb.document.PetDocument;
import org.springframework.stereotype.Repository;

@Repository
public interface MyMongoRepository extends OpaDataFilterMongoRepository<PetDocument, Long> {
}
