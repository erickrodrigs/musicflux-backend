package com.erickrodrigues.musicflux.favorite;

import java.util.List;

public interface FavoriteService {

    Favorite likeSong(Long userId, Long songId);

    void dislikeSong(Long userId, Long favoriteId);

    List<Favorite> findAllByUserId(Long userId);
}
