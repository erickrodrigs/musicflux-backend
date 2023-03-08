package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.favorite.Favorite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

    List<Favorite> findAllByUserId(Long userId);
}
