package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.hibernate;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
class MyRepository extends OpaGenericDataFilterDao<Pet> {

    public MyRepository() {
        setClazz(Pet.class);
    }
}