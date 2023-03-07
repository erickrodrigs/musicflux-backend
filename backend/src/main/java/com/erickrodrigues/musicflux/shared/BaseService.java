package com.erickrodrigues.musicflux.shared;

import org.springframework.data.repository.CrudRepository;

public abstract class BaseService {

    protected <T, ID> T getEntityOrThrowException(final ID entityId,
                                                  final CrudRepository<T, ID> repository,
                                                  final Class<T> tClass) {
        return repository
                .findById(entityId)
                .orElseThrow(() -> new ResourceNotFoundException(tClass.getSimpleName() + " with that ID does not exist"));
    }
}
