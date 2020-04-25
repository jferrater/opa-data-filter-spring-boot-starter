package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.repository.document.PetDocument;
import org.springframework.stereotype.Repository;

@Repository
public interface MyMongoRepository extends OpaDataFilterMongoRepository<PetDocument, Long> {
}