package com.erickrodrigues.musicflux.services;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public abstract class BaseService {

    protected <T, ID> T getEntityOrThrowException(final ID entityId,
                                                  final CrudRepository<T, ID> repository,
                                                  final Class<T> tClass) {
        final Optional<T> optionalEntity = repository.findById(entityId);

        if (optionalEntity.isEmpty()) {
            throw new RuntimeException(tClass.getSimpleName() + " with that ID does not exist");
        }

        return optionalEntity.get();
    }
}
