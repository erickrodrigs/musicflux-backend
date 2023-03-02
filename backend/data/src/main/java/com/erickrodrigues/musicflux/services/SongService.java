package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Song;

import java.util.List;

public interface SongService {

    void play(Long profileId, Long songId);

    List<Song> findAllByTitleContainingIgnoreCase(String text);

    List<Song> findAllByAlbumId(Long albumId);

    List<Song> findMostListenedSongsByArtistId(Long artistId);
}
