package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Favorite;
import org.springframework.data.repository.CrudRepository;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {}
