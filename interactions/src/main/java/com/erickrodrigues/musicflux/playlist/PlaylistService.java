package com.erickrodrigues.musicflux.playlist;

import java.util.List;

public interface PlaylistService {

    Playlist create(Long userId, String name);

    Playlist findById(Long playlistId);

    List<Playlist> findAllByNameContainingIgnoreCase(String text);

    List<Playlist> findAllByUserId(Long userId);

    Playlist addTracks(Long userId, Long playlistId, List<Long> tracksIds);

    Playlist removeTracks(Long userId, Long playlistId, List<Long> tracksIds);

    void deleteById(Long userId, Long playlistId);
}
