package com.erickrodrigues.musicflux.playlist;

import com.erickrodrigues.musicflux.playlist.Playlist;

import java.util.List;

public interface PlaylistService {

    Playlist create(Long userId, String name);

    Playlist findById(Long playlistId);

    List<Playlist> findAllByNameContainingIgnoreCase(String text);

    List<Playlist> findAllByUserId(Long userId);

    Playlist addSong(Long userId, Long playlistId, Long songId);

    Playlist removeSong(Long userId, Long playlistId, Long songId);

    void deleteById(Long userId, Long playlistId);
}
