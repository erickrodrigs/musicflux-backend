package com.erickrodrigues.musicflux.favorite;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    List<Favorite> findAllByUserId(Long userId);

    Optional<Favorite> findByTrackId(Long trackId);
}
