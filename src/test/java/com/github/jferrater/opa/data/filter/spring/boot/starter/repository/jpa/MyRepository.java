package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.entity.PetEntity;
import org.springframework.stereotype.Repository;

@Repository
class MyRepository extends OpaGenericDataFilterDao<PetEntity> {

    public MyRepository() {
        setClazz(PetEntity.class);
    }
}