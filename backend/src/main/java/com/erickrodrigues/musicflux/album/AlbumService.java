package com.erickrodrigues.musicflux.album;

import com.erickrodrigues.musicflux.album.Album;

import java.util.List;

public interface AlbumService {

    List<Album> findAllByTitleContainingIgnoreCase(String text);

    List<Album> findAllByArtistId(Long artistId);
}
