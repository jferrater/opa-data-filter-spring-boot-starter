package com.github.jferrater.opadatafilterjpaspringbootstarter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author joffryferrater
 *
 * @param <T> The managed entity
 * @param <ID> The id of the managed entity
 */
@NoRepositoryBean
public interface OpaDataFilterRepository<T, ID> extends JpaRepository<T, ID> {

    List<T> findAll();

}
