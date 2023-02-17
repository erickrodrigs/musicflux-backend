package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Album;

import java.util.Set;

public interface AlbumService {

    Set<Album> findAllByTitle(String title);

    Set<Album> findAllByArtistId(Long artistId);
}
