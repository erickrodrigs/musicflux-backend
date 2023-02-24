package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;

import java.util.Set;

public interface AlbumService {

    Set<Album> findAllByTitleContainingIgnoreCase(String text);

    Set<Album> findAllByArtistId(Long artistId);
}
