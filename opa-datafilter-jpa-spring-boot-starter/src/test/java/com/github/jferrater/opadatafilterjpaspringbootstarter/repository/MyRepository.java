package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.entity.PetEntity;
import org.springframework.stereotype.Repository;

/**
 * @author joffryferrater
 */
@Repository
class MyRepository extends OpaGenericDataFilterDao<PetEntity> {

    public MyRepository() {
        setClazz(PetEntity.class);
    }
}