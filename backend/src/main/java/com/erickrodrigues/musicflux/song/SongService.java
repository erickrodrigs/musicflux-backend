package com.erickrodrigues.musicflux.song;

import java.util.List;

public interface SongService {

    void play(Long profileId, Long songId);

    List<Song> findAllByTitleContainingIgnoreCase(String text);

    List<Song> findAllByAlbumId(Long albumId);

    List<Song> findMostPlayedSongsByArtistId(Long artistId);
}
