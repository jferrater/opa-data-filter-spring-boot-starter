package com.github.jferrater.opadatafiltermongospringbootstarter.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * @author joffryferrater
 *
 * @param <T> The class type
 * @param <ID> The class id
 */
@NoRepositoryBean
public interface OpaDataFilterMongoRepository<T, ID> extends MongoRepository<T, ID> {

    List<T> findAll();

    List<T> findAll(Sort sort);
}