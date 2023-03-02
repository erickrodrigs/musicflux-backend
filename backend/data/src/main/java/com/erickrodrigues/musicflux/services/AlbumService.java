package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;

import java.util.List;

public interface AlbumService {

    List<Album> findAllByTitleContainingIgnoreCase(String text);

    List<Album> findAllByArtistId(Long artistId);
}
