package com.erickrodrigues.musicflux.repositories;

import com.erickrodrigues.musicflux.domain.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre, Long> {}
