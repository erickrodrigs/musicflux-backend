package com.erickrodrigues.musicflux.services;

import org.springframework.data.repository.CrudRepository;

public abstract class BaseService {

    protected <T, ID> T getEntityOrThrowException(final ID entityId,
                                                  final CrudRepository<T, ID> repository,
                                                  final Class<T> tClass) {
        return repository
                .findById(entityId)
                .orElseThrow(() -> new RuntimeException(tClass.getSimpleName() + " with that ID does not exist"));
    }
}
