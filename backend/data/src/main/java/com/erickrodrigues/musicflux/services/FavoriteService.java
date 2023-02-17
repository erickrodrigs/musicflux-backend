package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Favorite;

import java.util.Set;

public interface FavoriteService {

    Favorite likeSong(Long profileId, Long songId);

    void dislikeSong(Long profileId, Long favoriteId);

    Set<Favorite> findAllByProfileId(Long profileId);
}
