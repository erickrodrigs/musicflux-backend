package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Favorite;

import java.util.List;

public interface FavoriteService {

    Favorite likeSong(Long profileId, Long songId);

    void dislikeSong(Long profileId, Long favoriteId);

    List<Favorite> findAllByProfileId(Long profileId);
}
