package com.erickrodrigues.musicflux.genre;

import com.erickrodrigues.musicflux.shared.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl extends BaseService implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        final List<Genre> genres = new ArrayList<>();
        genreRepository.findAll().forEach(genres::add);
        return genres;
    }
}
