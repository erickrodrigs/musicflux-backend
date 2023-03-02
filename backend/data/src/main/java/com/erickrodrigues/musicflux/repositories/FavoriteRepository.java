package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Favorite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    List<Favorite> findAllByProfileId(Long profileId);
}
