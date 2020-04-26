package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import com.github.jferrater.opadatafiltermongospringbootstarter.domain.PetProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface PetProfileRepository extends OpaDataFilterMongoRepository<PetProfile, String>{

}
