package com.joffryferrater.opadatafilterspringbootstarter.it;

import com.joffryferrater.opadatafilterspringbootstarter.service.OpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<Pet, Long>, OpaRepository<Pet> {

}
