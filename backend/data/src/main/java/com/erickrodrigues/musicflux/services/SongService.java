package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Song;

import java.util.Set;

public interface SongService {

    void play(Long profileId, Long songId);

    Set<Song> findAllByTitleContainingIgnoreCase(String text);

    Set<Song> findAllByAlbumId(Long albumId);

    Set<Song> findMostListenedSongsByArtistId(Long artistId);
}
