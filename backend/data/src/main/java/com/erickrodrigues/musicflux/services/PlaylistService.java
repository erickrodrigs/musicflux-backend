package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Playlist;

import java.util.List;

public interface PlaylistService {

    Playlist create(Long profileId, String name);

    Playlist findById(Long playlistId);

    List<Playlist> findAllByNameContainingIgnoreCase(String text);

    List<Playlist> findAllByProfileId(Long profileId);

    Playlist addSong(Long profileId, Long playlistId, Long songId);

    Playlist removeSong(Long profileId, Long playlistId, Long songId);

    void deleteById(Long profileId, Long playlistId);
}
