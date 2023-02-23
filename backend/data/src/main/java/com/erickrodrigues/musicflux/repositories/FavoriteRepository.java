package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Favorite;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    Set<Favorite> findAllByProfileId(Long profileId);
}
