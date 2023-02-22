package com.erickrodrigues.musicflux.services;

import com.erickrodrigues.musicflux.domain.Genre;
import com.erickrodrigues.musicflux.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GenreServiceImpl extends BaseService implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Set<Genre> findAll() {
        final Set<Genre> genres = new HashSet<>();
        genreRepository.findAll().forEach(genres::add);
        return genres;
    }
}
