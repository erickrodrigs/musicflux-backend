package com.erickrodrigues.musicflux.song;

import java.util.List;

public interface SongService {

    void play(Long userId, Long songId);

    List<Song> findAllByTitleContainingIgnoreCase(String text);

    List<Song> findAllByGenreName(String genreName);

    List<Song> findAllByAlbumId(Long albumId);

    List<Song> findMostPlayedSongsByArtistId(Long artistId);
}
