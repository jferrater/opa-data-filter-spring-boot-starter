package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpaRepository<T, ID> extends JpaRepository<T, ID> {

    List<T> findAll();

}
