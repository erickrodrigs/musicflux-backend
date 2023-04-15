package com.erickrodrigues.musicflux.favorite;

import com.erickrodrigues.musicflux.track.Track;

import java.util.List;
import java.util.Map;

public interface FavoriteService {

    Favorite likeTrack(Long userId, Long trackId);

    void dislikeTrack(Long userId, Long favoriteId);

    List<Favorite> findAllByUserId(Long userId);

    Map<Track, Boolean> checkWhetherTracksAreLiked(Long userId, List<Long> tracksIds);
}
