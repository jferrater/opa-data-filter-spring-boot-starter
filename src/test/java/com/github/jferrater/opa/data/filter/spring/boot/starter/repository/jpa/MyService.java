package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa.entity.PetEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
