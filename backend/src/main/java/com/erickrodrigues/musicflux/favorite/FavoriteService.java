package com.erickrodrigues.musicflux.favorite;

import java.util.List;

public interface FavoriteService {

    Favorite likeSong(Long profileId, Long songId);

    void dislikeSong(Long profileId, Long favoriteId);

    List<Favorite> findAllByProfileId(Long profileId);
}
