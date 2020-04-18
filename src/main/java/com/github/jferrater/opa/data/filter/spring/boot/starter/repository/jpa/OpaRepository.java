package com.github.jferrater.opa.data.filter.spring.boot.starter.repository.jpa;

import java.util.List;

public interface OpaRepository<T, ID>  {

    List<T> filteredResults();

}
