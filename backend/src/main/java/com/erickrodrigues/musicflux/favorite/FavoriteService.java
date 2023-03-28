package com.erickrodrigues.musicflux.favorite;

import java.util.List;

public interface FavoriteService {

    Favorite likeTrack(Long userId, Long trackId);

    void dislikeTrack(Long userId, Long favoriteId);

    List<Favorite> findAllByUserId(Long userId);
}
