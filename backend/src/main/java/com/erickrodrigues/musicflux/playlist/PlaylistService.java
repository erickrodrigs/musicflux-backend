package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.playlist.Playlist;

import java.util.List;

public interface PlaylistService {

    Playlist create(Long userId, String name);

    Playlist findById(Long playlistId);

    List<Playlist> findAllByNameContainingIgnoreCase(String text);

    List<Playlist> findAllByUserId(Long userId);

    Playlist addTrack(Long userId, Long playlistId, Long trackId);

    Playlist removeTrack(Long userId, Long playlistId, Long trackId);

    void deleteById(Long userId, Long playlistId);
}
