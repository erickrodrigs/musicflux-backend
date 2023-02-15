package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Favorite;

import java.util.Set;

public interface FavoriteService {

    void likeSong(Long profileId, Long songId);

    void dislikeSong(Long profileId, Long songId);

    Set<Favorite> findAllByProfileId(Long profileId);
}
