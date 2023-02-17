package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Genre;

import java.util.Set;

public interface GenreService {

    Set<Genre> findAll();
}
