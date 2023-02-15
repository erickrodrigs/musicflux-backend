package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Artist;

import java.util.Set;

public interface ArtistService {

    Set<Artist> findAllByName(String name);
}
