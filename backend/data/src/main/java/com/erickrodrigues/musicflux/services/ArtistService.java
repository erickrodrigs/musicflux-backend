package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Artist;

import java.util.List;

public interface ArtistService {

    List<Artist> findAllByNameContainingIgnoreCase(String text);
}
