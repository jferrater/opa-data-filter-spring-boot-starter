package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import com.github.jferrater.opadatafilterjpaspringbootstarter.entity.PetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author joffryferrater
 */
@Service
public class MyService {

    private MyOpaDataFilterRepository myOpaRepository;

    @Autowired
    public MyService(MyOpaDataFilterRepository myOpaRepository) {
        this.myOpaRepository = myOpaRepository;
    }

    public List<PetEntity> getPets() {
        return myOpaRepository.findAll();
    }
}
