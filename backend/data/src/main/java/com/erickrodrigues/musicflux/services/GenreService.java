package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Genre;

import java.util.List;

public interface GenreService {

    List<Genre> findAll();
}
