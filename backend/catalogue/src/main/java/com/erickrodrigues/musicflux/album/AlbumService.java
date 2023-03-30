package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.track.Track;

import java.util.List;

public interface AlbumService {

    List<Album> findAllByTitleContainingIgnoreCase(String text);

    List<Track> getAlbumTracks(Long albumId);
}
