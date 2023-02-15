package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Song;

import java.util.Set;

public interface SongService {

    void play(Long profileId);

    Set<Song> findAllByTitle(String title);

    Set<Song> findAllByAlbumId(Long albumId);

    Set<Song> findMostListenedSongsByArtistId(Long artistId);
}
