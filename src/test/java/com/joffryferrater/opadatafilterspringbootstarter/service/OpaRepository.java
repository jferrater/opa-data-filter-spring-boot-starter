package com.joffryferrater.opadatafilterspringbootstarter.service;

import com.joffryferrater.opadatafilterspringbootstarter.model.request.PartialRequest;

import java.util.List;

public interface OpaRepository<T> {

    List<T> findAll(PartialRequest partialRequest);
}
