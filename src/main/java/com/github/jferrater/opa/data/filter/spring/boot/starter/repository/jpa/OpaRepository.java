package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface OpaRepository<T, ID> extends JpaRepository<T, ID> {

    List<T> findAll();

}
