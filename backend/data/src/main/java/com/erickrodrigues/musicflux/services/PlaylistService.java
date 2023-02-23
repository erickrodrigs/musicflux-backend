package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Playlist;

import java.util.Set;

public interface PlaylistService {

    Playlist create(Long profileId, String name);

    Playlist findById(Long playlistId);

    Set<Playlist> findAllByName(String name);

    Set<Playlist> findAllByProfileId(Long profileId);

    Playlist addSong(Long profileId, Long playlistId, Long songId);

    Playlist removeSong(Long profileId, Long playlistId, Long songId);

    void deleteById(Long profileId, Long playlistId);
}
