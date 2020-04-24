package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.entity.PetEntity;
import org.springframework.stereotype.Repository;

/**
 * @author joffryferrater
 */
@Repository
public interface MyOpaDataFilterRepository extends OpaDataFilterRepository<PetEntity, Long> {

}
